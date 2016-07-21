package xyz.enhorse.fontic;

import xyz.enhorse.commons.Folder;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         13/07/16
 */
public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {
        if (args.length == 0) {
            int counter = 0;

            for (Path path : new Folder().listFiles("otf", "ttf", "TTF")) {
                if (new FontFile(path).renameToNormalized()) {
                    counter++;
                }
            }
            System.out.println(counter + " files was renamed.");
        } else {
            String filename = args[0];
            FontFile fontFile = new FontFile(filename);
            if (fontFile.renameToNormalized()) {
                System.out.println(filename + " was renamed to " + fontFile.originalFilename());
            }
        }

    }
}
