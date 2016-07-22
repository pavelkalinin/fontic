package xyz.enhorse.fontic;

import xyz.enhorse.commons.HandyPath;
import xyz.enhorse.commons.Validate;

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

    private final Font font;
    private HandyPath file;


    FontFile(final Path path) {
        font = loadFont(loadFile(Validate.notNull("input file", path)));
    }


    FontFile(final String file) {
        this(Paths.get(Validate.notNullOrEmpty("input file", file)));
    }


    FontFile(final File file) {
        this(Validate.notNull("input file", file).toPath());
    }


    Path source() {
        return file;
    }


    String family() {
        return font.getFamily().trim();
    }


    String style() {
        String name = font.getFontName().trim();
        String family = family();

        if (name.startsWith(family)) {
            name = name.substring(family.length()).trim();
        }

        return name.isEmpty() ? REGULAR_FONT_MARK : name;
    }


    boolean hasCyrillic() {
        return font.canDisplay(CYRILLIC_SPECIAL);
    }


    String normalized() {
        return file.pathname()
                + family()
                + (hasCyrillic() ? CYRILLIC_FONT_MARK : "")
                + FAMILY_STYLE_SEPARATOR
                + removeSymbols(style())
                + file.extension();
    }


    boolean renameToNormalized() {
        if (!file.isWritable()) {
            return false;
        }

        File newFile = new File(normalized());
        boolean result = file.toFile().renameTo(newFile);
        if (result) {
            loadFile(newFile.toPath());
        }

        return result;
    }


    private File loadFile(final Path path) {
        file = new HandyPath(path);

        if (!file.isExisting()) {
            throw new IllegalArgumentException("The file \'" + file + "\' doesn't exist!");
        }
        if (!file.isReadable()) {
            throw new IllegalArgumentException("Cannot get read-access to the file \'" + file + '\'');
        }

        return file.toFile();
    }


    private static Font loadFont(final File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            return Font.createFont(Font.TRUETYPE_FONT, input);
        } catch (IOException | FontFormatException ex) {
            throw new IllegalStateException(ex);
        }
    }


    private static String removeSymbols(final String string) {
        return string.replaceAll("[,.;_-]|\'|\"", "");
    }
}
