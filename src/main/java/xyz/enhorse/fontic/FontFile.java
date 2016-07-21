package xyz.enhorse.fontic;

import xyz.enhorse.commons.HandyPath;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         14.07.2016
 */
class FontFile {

    private static final char FAMILY_STYLE_SEPARATOR = '-';
    private static final String REGULAR_FONT_MARK = "Regular";
    private static final String CYRILLIC_FONT_MARK = "(Cyrillic)";
    private static final char CYRILLIC_SPECIAL = 'Ё';

    private final HandyPath file;
    private final Font font;


    FontFile(final Path path) {
        file = loadFile(path);
        font = loadFont(file.toFile());
    }


    FontFile(final String path) {
        file = loadFile(Paths.get(path));
        font = loadFont(file.toFile());
    }


    public File normalizedFilename() {
        String filename = file.pathname()
                + HandyPath.PATH_SEPARATOR
                + normalizedFontName()
                + HandyPath.EXTENSION_SEPARATOR
                + file.extension();
        return new File(filename);
    }


    private String normalizedFontName() {
        return family()
                + (hasCyrillic() ? CYRILLIC_FONT_MARK : "")
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


    public String originalFilename() {
        return file.filename();
    }


    public boolean renameToNormalized() {
        if (file.isWritable()) {
            return file.toFile().renameTo(normalizedFilename());
        }

        throw new IllegalStateException("Cannot get write-access to the file \'" + file.filename() + '\'');
    }


    private static HandyPath loadFile(final Path file) {
        final HandyPath path = new HandyPath(file);

        if (!path.isExisting()) {
            throw new IllegalArgumentException("The file \'" + file + "\' doesn't exist!");
        }
        if (!path.isReadable()) {
            throw new IllegalArgumentException("Cannot get read-access to the file \'" + file + '\'');
        }

        return path;
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
}
