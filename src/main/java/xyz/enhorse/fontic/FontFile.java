package xyz.enhorse.fontic;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         14.07.2016
 */
class FontFile {

    private static final char FAMILY_STYLE_SEPARATOR = '-';
    private static final String REGULAR_FONT_MARK = "Regular";
    private static final String CYRILLIC_FONT_MARK = "(Cyrillic)";
    private static final char CYRILLIC_SPECIAL = 'Ё';

    private final File file;
    private final Font font;


    FontFile(final String filename) {
        file = loadFile(filename);
        font = loadFont(file);
    }


    public File normalizedFilename() {
        String filename = getPath() + normalizedFontName() + getExtension();
        return new File(filename);
    }


    private String normalizedFontName() {
        String cyrillic = "";

        if (hasCyrillic()) {
            cyrillic = CYRILLIC_FONT_MARK;
        }

        return family()
                + cyrillic
                + FAMILY_STYLE_SEPARATOR
                + removeSymbols(style());
    }


    public String family() {
        return font.getFamily().trim();
    }


    public String style() {
        String name = font.getFontName().trim();
        String family = family();

        if (name.startsWith(family)) {
            name = name.substring(family.length()).trim();
        }

        return name.isEmpty() ? REGULAR_FONT_MARK : name;
    }


    public boolean hasCyrillic() {
        return font.canDisplay(CYRILLIC_SPECIAL);
    }


    private String getExtension() {
        final char separator = '.';
        String extension = file.getName();

        return extension.contains(separator + "")
                ? extension.substring(extension.lastIndexOf(separator))
                : "";
    }


    private String getPath() {
        File parent = file.getParentFile();

        return parent != null
                ? parent.getAbsolutePath() + File.separator
                : "";
    }


    public String originalFilename() {
        return file.getName();
    }


    private static File loadFile(final String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            throw new IllegalArgumentException("The file \'" + filename + "\' doesn't exit!");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("Cannot get read-access to the file \'" + filename + '\'');
        }

        return file;
    }


    private static Font loadFont(final File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            return Font.createFont(Font.TRUETYPE_FONT, input);
        } catch (IOException | FontFormatException ex) {
            throw new IllegalStateException(ex);
        }
    }


    private static String removeSymbols(final String string) {
        final char[] symbols = {',', '.', ';', '_', '-', '\'', '\"'};

        String result = string;
        for (char symbol : symbols) {
            result = result.replace(symbol, '\0');
        }

        return result;
    }


    public boolean renameToNormalized() {
        if (file.canWrite()) {
            return file.renameTo(normalizedFilename());
        }

        throw new IllegalStateException("Cannot get write-access to the file \'" + file.getName() + '\'');
    }


    public static boolean renameToNormalized(String filename) {
        FontFile fontFile = new FontFile(filename);
        return fontFile.renameToNormalized();
    }
}
