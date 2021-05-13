import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.HashMap;

public class ImageManager {
    private static ImageManager instance = null; // keeps track of Singleton instance

    private String directory = "images";
    HashMap<String, Image> images;

    private ImageManager() {
        images = new HashMap<String, Image>();

        // Auto load all music from sounds folder
        File directoryPath = new File(directory);
        String contents[] = directoryPath.list();
        Image image;
        for (int i = 0; i < contents.length; i++) {
            image = loadImage(directory + "/" + contents[i]);
            images.put(contents[i], image);

        }
    }

    private Image loadImage(String filename) {
        return new ImageIcon(filename).getImage();
    }

    public static ImageManager getInstance() { // class method
        if (instance == null)
            instance = new ImageManager();
        return instance;
    }

    public Image getImage(String filename) {
        return images.get(filename);
    }

}
