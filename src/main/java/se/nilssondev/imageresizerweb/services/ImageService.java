package se.nilssondev.imageresizerweb.services;

import java.util.List;
import java.io.File;

public interface ImageService {
    boolean save(File file);
    File getImage(String id);
    void delete(String id);
}
