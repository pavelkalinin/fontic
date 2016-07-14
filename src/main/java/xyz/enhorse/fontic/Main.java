package xyz.enhorse.fontic;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         13/07/16
 */
public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {
        if (args.length == 0) {
            int counter = 0;
            for (File file : new TaskFolder("").getContents("otf", "ttf")) {
                if (FontFile.renameToNormalized(file.toString())) {
                    counter++;
                }
            }
            System.out.println(counter + " files was renamed");
        } else {
            String filename = args[0];
            FontFile fontFile = new FontFile(filename);
            if (fontFile.renameToNormalized()) {
                System.out.println(filename + " was renamed to " + fontFile.originalFilename());
            }
        }

    }
}
