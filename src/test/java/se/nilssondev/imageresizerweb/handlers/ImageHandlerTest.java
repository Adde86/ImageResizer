package se.nilssondev.imageresizerweb.handlers;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ImageHandlerTest {


    private static LogCaptor logCaptor;

    ImageHandler imageHandler;


    @ParameterizedTest
    @MethodSource("provideStringsForValidateFileIsImage")
    void validateFileIsImage(String file, boolean expected) {

        assertEquals(expected, imageHandler.validateImage(file));

    }

    private static Stream<Arguments> provideStringsForValidateFileIsImage() {
        return Stream.of(
                Arguments.of("file.jpg", true),
                Arguments.of("file.png", true),
                Arguments.of("file.jpeg", true),
                Arguments.of("file.pdf", true),
                Arguments.of("file.txt", false),
                Arguments.of("file.csv", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilesForValidateFile")
    void validateFile(File file, boolean expected) throws Exception {
        assertEquals(expected, imageHandler.validateFile(file));
    }

    private static Stream<Arguments> provideFilesForValidateFile() {
        return Stream.of(
                Arguments.of(Paths.get("src/main/resources/static/lfc2.jpg").toFile(), true),
                Arguments.of(Paths.get("src/main/resources/static/").toFile(), false)
        );
    }

    @Test
    void resizeImage() throws IOException {
        BufferedImage imageToResize = ImageIO.read(Paths.get("src/main/resources/static/temp/lfc2.jpg").toFile());
        BufferedImage resizedImage = imageHandler.resizeImage(imageToResize);
        assertEquals(200, resizedImage.getHeight());
        assertEquals(200, resizedImage.getWidth());
    }

    @Test
    void createImageFromFile() {
        File file = Paths.get("src/main/resources/static/lfc4.jpg").toFile();
        assertEquals(BufferedImage.class, imageHandler.createImageFromFile(file).getClass());
    }

    @Test
    void ImageResizedExistsNewName() throws IOException {
        File file = Paths.get("src/main/resources/static/lfc4.jpg").toFile();
        File newFile = imageHandler.resizeFile(file);
        assertEquals("lfc4_thumb.jpg", newFile.getName());
    }

    @Test
    void validateNullFile() throws IOException {
        File file = null;
        assertFalse(imageHandler.validateFile(file));
        assertThat(logCaptor.getErrorLogs()).contains("Provided file is null");
    }

    @Test
    void fileValidationNotAFile(){
        File file = Paths.get("src/main/resources/static").toFile();
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