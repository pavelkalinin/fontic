package xyz.enhorse.fontic;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         14.07.2016
 */
public class TaskFolder {

    private final File folder;


    public TaskFolder(String directory) {
        folder = validateDirectory(directory);
    }


    public List<File> getContents(final String... extensions) {
        if (extensions == null) {
            return Collections.emptyList();
        }

        List<File> result = new ArrayList<>();
        for (String extension : extensions) {
            FileFilter filter = new FileFilter(extension);
            result.addAll(fileList(filter));
        }
        return result;
    }


    private File validateDirectory(String path) {
        File result = null;

        if (path != null) {
            File temp = new File(path);
            if (temp.isDirectory()) {
                result = temp;
            }
        }

        return result != null
                ? result
                : currentDirectory();
    }


    private File folder() {
        return folder;
    }


    private File currentDirectory() {
        return new File("");
    }


    private List<File> fileList(FileFilter filter) {
        List<File> result = new ArrayList<>();
        Path current = folder().toPath();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(current, filter)) {
            for (Path path : directoryStream) {
                if (!Files.isDirectory(path))
                    result.add(path.toFile());
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Listing error with the folder \'" + folder() + '\'', ex);
        }

        return result;
    }


    private class FileFilter implements DirectoryStream.Filter<Path> {

        private final Set<String> suitable = new HashSet<>();


        FileFilter(final String... extensions) {
            fillSuitable(extensions);
        }


        private void fillSuitable(final String... extensions) {
            for (String extension : extensions) {
                extension = extension.trim();
                if (!extension.isEmpty()) {
                    if (extension.charAt(0) != '.') {
                        extension = '.' + extension;
                    }
                    suitable.add(extension);
                }
            }
        }


        @Override
        public boolean accept(Path entry) throws IOException {
            return suitable.contains(entry.toString().toLowerCase());
        }
    }
}

