package se.nilssondev.imageresizerweb.implementations;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.nilssondev.imageresizerweb.handlers.ImageHandler;
import se.nilssondev.imageresizerweb.services.ImageService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ImageImplAWSTest {

    static String imageFolder;

    File image;

    ImageService aws;

    final String TEST_BUCKET = "image-resizer-images";

    public ImageImplAWSTest(){
        aws = new ImageImplAWS();
    }

    @Test
    void save() {
        assertTrue(aws.save(image, TEST_BUCKET));
    }


    @Test
    void getDefaultImage() {


        assertEquals("lfc.jpg", aws.getImage("lfc.jpg", TEST_BUCKET).getName());
        try {
            Files.delete(Paths.get("lfc.jpg"));
        }catch (Exception e){
            System.out.println("file not found");
        }
    }

    @Test
    void getImageIdDoesntExist(){
        String wrongId = "not-here";
        assertNull(aws.getImage(wrongId, TEST_BUCKET));
    }

    @Test
    void delete() {
        aws.save(image, "image-resizer-images");
        assertEquals(image.getName(), aws.getImage(image.getName(), TEST_BUCKET).getName());
        aws.delete(image.getName(), TEST_BUCKET);

        assertNull(aws.getImage(image.getName(), TEST_BUCKET));
        aws.save(new File("lfc.jpg"), TEST_BUCKET);
    }



    @BeforeAll
    static void beforeAll() {

        if (Files.isDirectory(Paths.get("target/classes/static"))){
            imageFolder = "target/classes/static";
        }else {
            imageFolder = "src/main/resources/static";
        }


    }

    @BeforeEach
    void setUp() {
         image = Paths.get(imageFolder+"/lfc.jpg").toFile();
    }
}