package xyz.enhorse.fontic;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         13/07/16
 */
public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {
        System.out.println("I'm fontic!");
        String filename = (args.length > 0) ? args[0] : Main.class.getResource(File.separator + "test_cyrillic.otf")
                .getPath();
        File file = new File(filename);
        Font font = getFont(file);
        String newFilename = getPath(file) + generateFontName(font) + getExtension(file);
        File newFile = new File(newFilename);
        System.out.println(file + " => " + newFile);
    }


    private static Font getFont(final File file) throws IOException, FontFormatException {
        FileInputStream input = new FileInputStream(file);
        Font font = Font.createFont(Font.TRUETYPE_FONT, input);
        input.close();
        return font;
    }

    private static String generateFontName(final Font font) {
        String family = font.getFamily().trim();
        String name = font.getFontName().trim();


        if (name.startsWith(family)) {
            name = name.trim().replace(family, "");
        }

        return family
                + (font.canDisplay('ё') ? "(Cyrillic)" : "")
                + '-'
                + removeSymbols(name);
    }


    private static String removeSymbols(final String string) {
        final char[] symbols = {',', '.', ';', '_', '-', '\'', '\"'};

        String result = string;
        for (char symbol : symbols) {
            result = result.replace(symbol, '\0');
        }

        return result;
    }


    private static String getExtension(final File file) {
        String extension = file.getName();
        return extension.contains(".")
                ? extension.substring(extension.lastIndexOf('.'))
                : "";
        }

    private static String getPath(final File file) {
        File parent = file.getParentFile();

        return parent != null
                ? parent.getAbsolutePath() + File.separator
                : "";
    }
}
