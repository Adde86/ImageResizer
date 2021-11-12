package se.nilssondev.imageresizerweb.services;

import java.util.List;
import java.io.File;

public interface ImageService {
    boolean save(File file, String bucketName);
    File getImage(String id, String bucketName);
    void delete(String id, String bucketName);
}
