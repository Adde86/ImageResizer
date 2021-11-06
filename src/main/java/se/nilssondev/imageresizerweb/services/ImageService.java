package se.nilssondev.imageresizerweb.services;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageService {
    void save(File file);
    List<String> getImages();
    String getImage(String id);
    void delete(String id);
}
