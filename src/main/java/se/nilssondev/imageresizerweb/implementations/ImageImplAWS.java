package se.nilssondev.imageresizerweb.implementations;

import org.springframework.stereotype.Service;
import se.nilssondev.imageresizerweb.services.ImageService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageImplAWS implements ImageService {

    private final S3Client s3;
    private final String BUCKET_NAME = "image-resizer-images";

    public ImageImplAWS() {

        this.s3 =  S3Client.builder().region(Region.EU_WEST_1).build();

    }

    @Override
    public void save(File file) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET_NAME).key(file.getName()).build();
        RequestBody body = RequestBody.fromFile(file);

        s3.putObject(request, body);
    }

    @Override
    public List<String> getImages() {


        List<String> imageUrls = new ArrayList<>();
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(BUCKET_NAME)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();

           for(S3Object object : objects){
                imageUrls.add("https://"+BUCKET_NAME+".s3.amazonaws.com/"+object.key());

           }

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return imageUrls;
    }

    @Override
    public String getImage(String id) throws  AwsServiceException, SdkClientException {
        GetObjectRequest request = GetObjectRequest.builder().bucket(BUCKET_NAME).key(id).build();
        GetObjectResponse response = s3.getObject(request).response();

       return "https://"+BUCKET_NAME+".s3.amazonaws.com/"+id;
    }

    @Override
    public void delete(String id) {
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(id).build();
        s3.deleteObject(request);
    }
}
