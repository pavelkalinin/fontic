package xyz.enhorse.fontic;

import java.io.File;
import java.nio.file.Path;

/**
 * 09.12.2015.
 */
public class AFile {
    public static final char EXTENSION_SEPARATOR = '.';

    private String path;
    private String name;
    private String extension;

    public AFile(final String filename) {
        set(filename);
    }

    public AFile(final File file) {
        if (file != null) {
            set(file.getName());
        } else {
            set("");
        }
    }

    public AFile(final Path path) {
        if (path != null) {
            set(path.toString());
        } else {
            set("");
        }
    }

    public AFile(final String name, final String extension) {
        setName(name);
        setExtension(extension);
    }

    public AFile set(final String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            setName(parseName(filename));
            setExtension(parseExtension(filename));
        } else {
            setName(null);
            setExtension(null);
        }
        return this;
    }

    public AFile setName(final String value) {
        name = (value != null)
                ? value
                : "";
        return this;
    }

    public AFile setExtension(final String value) {
        extension = (value != null)
                ? value
                : "";
        return this;
    }

    public String get() {
        return (getExtension().length() > 0)
                ? name + EXTENSION_SEPARATOR + extension
                : name;
    }

    public String getName() {
        return (name != null)
                ? name
                : "";
    }

    public String getExtension() {
        return (extension != null)
                ? extension
                : "";
    }

    private String parseExtension(final String string) {
        String result = "";
        if (string != null) {
            int index = indexOfExtension(string);
            result = (index != -1)
                    ? string.substring(indexOfExtension(string) + 1)
                    : "";
        }
        return result;
    }

    private String parseName(final String string) {
        String result = "";
        if (string != null) {
            result = removeExtension(removePath(string));
        }
        return result;
    }

    private String removeExtension(final String string) {
        String result = "";
        if (string != null) {
            int index = indexOfExtension(string);
            result = (index != -1)
                    ? string.substring(0, index)
                    : string;
        }
        return result;
    }

    private String removePath(final String string) {
        String result = "";
        if (string != null) {
            int index = indexOfLastSeparator(string);
            result = string.substring(index + 1);
        }
        return result;
    }

    private static int indexOfExtension(String string) {
        int result = -1;
        if (string != null) {
            int extensionIndex = string.lastIndexOf(EXTENSION_SEPARATOR);
            int separatorIndex = indexOfLastSeparator(string);
            result = (separatorIndex > extensionIndex)
                    ? -1
                    : extensionIndex;
        }
        return result;
    }

    private static int indexOfLastSeparator(String string) {
        int result = -1;
        if (string != null) {
            result = string.lastIndexOf(File.separator);
        }
        return result;
    }
}
