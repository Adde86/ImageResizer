package se.nilssondev.imageresizerweb.handlers;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.nilssondev.imageresizerweb.implementations.ImageImplAWS;
import se.nilssondev.imageresizerweb.services.ImageService;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageHandlerTest {

    private File image;

    private static LogCaptor logCaptor;

    private final ImageHandler imageHandler;

    static String imageFolder;

    ImageHandlerTest() {
        ImageService imageService = new ImageImplAWS();
        this.imageHandler = new ImageHandler();
        this.image = imageService.getImage("lfc.jpg", "image-resizer-images");
    }


    @Test
    void validateFileIsImage() {
        assertTrue(imageHandler.validateImage("file.jpg"));
        assertTrue(imageHandler.validateImage("file.png"));
        assertTrue(imageHandler.validateImage("file.jpeg"));
        assertFalse(imageHandler.validateImage("file.txt"));
        assertFalse(imageHandler.validateImage("file.csv"));

    }

    @Test
    void validateFile() {
        assertTrue(imageHandler.validateFile(image));

    }


    @Test
    void resizeImage() throws IOException {

        BufferedImage imageToResize = ImageIO.read(image);
        BufferedImage resizedImage = imageHandler.resizeImage(imageToResize);
        assertEquals(200, resizedImage.getHeight());
        assertEquals(200, resizedImage.getWidth());
    }

    @Test
    void createImageFromFile() throws IOException {
        assertEquals(BufferedImage.class, imageHandler.createImageFromFile(image).getClass());

    }

    @Test
    void ImageResizedExistsNewName() throws IOException {
        File file = new File("lfc4.jpg");
        File newFile = imageHandler.resizeFile(file);
        assertEquals("lfc4_thumb.jpg", newFile.getName());
        file.delete();
        newFile.delete();
    }

    @Test
    void validateNullFile() throws IOException {
        File file = null;
        assertFalse(imageHandler.validateFile(file));
        assertThat(logCaptor.getErrorLogs()).contains("Provided file is null");
    }

    @Test
    void fileValidationNotAFile(){
        File file = Paths.get("/").toFile();
        assertNull(imageHandler.createImageFromFile(file));

    }



    @BeforeAll
    static void beforeAll() {
        logCaptor = LogCaptor.forClass(ImageHandler.class);


    }

    @AfterEach
    void tearDown() {
        logCaptor.clearLogs();
    }
}