package se.nilssondev.imageresizerweb.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.nilssondev.imageresizerweb.services.ImageService;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.*;

@Service
@Slf4j
public class ImageImplAWS implements ImageService {

    private final S3Client s3;


    public ImageImplAWS() {

        this.s3 =  S3Client.builder().region(Region.EU_WEST_1).build();

    }

    @Override
    public boolean save(File file, String bucketName) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(file.getName()).build();
        RequestBody body = RequestBody.fromFile(file);

        PutObjectResponse response = s3.putObject(request, body);
        return response.sdkHttpResponse().isSuccessful();
    }


    @Override
    public File getImage(String id, String bucketName) throws  AwsServiceException, SdkClientException {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(id).build();

        try {
            byte[] bytes = s3.getObject(request).readAllBytes();

           try( FileOutputStream fileOut = new FileOutputStream(id)) {
               fileOut.write(bytes);

           }catch(IOException e){
               log.error(e.getMessage());
               return null;
           }
        }catch(Exception e){
            log.error(e.getMessage());
            return null;

        }
        return new File(id);

    }

    @Override
    public void delete(String id, String bucketName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(id).build();
        s3.deleteObject(request);
    }
}
