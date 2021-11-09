package se.nilssondev.imageresizerweb.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.nilssondev.imageresizerweb.services.ImageService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
public class ImageHandler {

    private final ImageService imageService;

    private static String[] acceptedFileSuffixes = new String[]{".jpeg", ".jpg", ".png", ".pdf" };

    public ImageHandler(ImageService imageService) {
        this.imageService = imageService;
    }

    public boolean validateImage(String file) {
        String suffix = file.substring(file.lastIndexOf('.'));
        return Arrays.asList(acceptedFileSuffixes).contains(suffix);
    }

    public boolean validateFile(File file)  {
        try {
            return file.isFile();
        }catch(NullPointerException e){
            log.error("Provided file is null");
            return false;
        }
    }

    public BufferedImage resizeImage(BufferedImage imageToResize) {
       BufferedImage resized = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
       Graphics2D graphics2D = resized.createGraphics();
       graphics2D.drawImage(imageToResize,0,0,200,200,null);
       graphics2D.dispose();

       return resized;
    }

    public BufferedImage createImageFromFile(File file) {
        try {
            if (validateFile(file) && validateImage(file.getName())) {
                return ImageIO.read(file);
            }
        }catch(IOException e){
            log.error("Invalid file: " +file.getPath());
        }
        return null;
    }

    public void resizeAndSave(File file) {
        BufferedImage imageToSave = resizeImage(createImageFromFile(file));
        File fileToSave = ImageIO.getCacheDirectory();
       imageService.save(fileToSave);
    }
}
