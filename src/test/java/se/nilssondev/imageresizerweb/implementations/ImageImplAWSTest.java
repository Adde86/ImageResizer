package se.nilssondev.imageresizerweb.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.nilssondev.imageresizerweb.services.ImageService;
import software.amazon.awssdk.services.s3.S3Client;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ImageImplAWSTest {

    @Mock
    S3Client client;
    @InjectMocks
    ImageService aws;

    File image;

    public ImageImplAWSTest(){
        aws = new ImageImplAWS();
    }

    @Test
    void save() {
        assertTrue(aws.save(image));
    }


    @Test
    void getDefaultImage() {

        assertEquals("lfc.jpg", aws.getImage("lfc.jpg").getName());
        try {
            Files.delete(Paths.get("src/main/resources/static/temp/lfc.jpg"));
        }catch (Exception e){
            System.out.println("file not found");
        }
    }

    @Test
    void getImageIdDoesntExist(){
        String wrongId = "not-here";
        assertNull(aws.getImage(wrongId));
    }

    @Test
    void delete() {
        aws.save(image);
        assertEquals(image.getName(), aws.getImage(image.getName()).getName());
        aws.delete(image.getName());
        assertNull(aws.getImage(image.getName()));

    }

    @BeforeEach
    void setUp() {
         image = Paths.get("src/main/resources/static/lfc2.jpg").toFile();
    }
}