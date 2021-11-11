package se.nilssondev.imageresizerweb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class S3ObjectModel {

    private String bucketName;
    private String url;
    private String key;

    public S3ObjectModel(String bucketName, String url, String key){
        this.bucketName = bucketName;
        this.url = url;
        this.key = key;
    }
}
