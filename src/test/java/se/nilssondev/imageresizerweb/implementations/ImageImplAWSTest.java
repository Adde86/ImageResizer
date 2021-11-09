package se.nilssondev.imageresizerweb.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;

import se.nilssondev.imageresizerweb.services.ImageService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ImageImplAWSTest {

    @MockBean
    S3Client client;


    ImageService aws;

    File image;

    public ImageImplAWSTest(){
        aws = new ImageImplAWS();
    }

    @Test
  void save() {
//        //when(client.putObject(image)).doReturn()
        aws.save(image);
        assertEquals("https://image-resizer-images.s3.amazonaws.com/"+image.getName(), aws.getImage(image.getName()));

    }

    @Test
    void getImages() {
        List<String> urls = new ArrayList<>();
        urls = aws.getImages();
        assert(urls.size() > 0);
        assert(urls.get(0).startsWith("https://image-resizer-images.s3.amazonaws.com/"));
    }


    @Test
    void getDefaultImage() {
        assertEquals("https://image-resizer-images.s3.amazonaws.com/lfc.jpg", aws.getImage("lfc.jpg"));
    }

    @Test
    void getImageIdDoesntExist(){
        String wrongId = "not-here";
        assertThrows(NoSuchKeyException.class,() -> {
            aws.getImage(wrongId);
        });
    }

    @Test
    void delete() {
        aws.save(image);
        assertEquals("https://image-resizer-images.s3.amazonaws.com/"+image.getName(), aws.getImage(image.getName()));
        aws.delete(image.getName());
        assertThrows(NoSuchKeyException.class, () -> {
            aws.getImage(image.getName());
        });

    }

    @BeforeEach
    void setUp() {
         image = Paths.get("src/main/resources/static/lfc2.jpg").toFile();
    }
}