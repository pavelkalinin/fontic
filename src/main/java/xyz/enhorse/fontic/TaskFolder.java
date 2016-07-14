package xyz.enhorse.fontic;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         14.07.2016
 */
public class TaskFolder {

    private final Path path;


    public TaskFolder(String path) {
        this.path = isValidPath(path)
                ? Paths.get(path)
                : currentDirectory();
    }


    public List<File> getContents(String... extensions) {
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


    private boolean isValidPath(String path) {
        return (path != null) && (new File(path).isDirectory());
    }


    private File path() {
        return path.toFile();
    }


    private Path currentDirectory() {
        return Paths.get("");
    }


    private List<File> fileList(FileFilter filter) {
        List<File> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path().toPath(), filter)) {
            for (Path path : directoryStream) {
                if (!Files.isDirectory(path))
                    fileNames.add(path.toFile());
            }
        } catch (IOException ex) {
            try {
                if (path().isFile() && filter.accept(path().toPath())) {
                    fileNames.add(path());
                }
            } catch (IOException e) {
                System.err.println("Listing error with the path \'" + path() + '\'');
                e.printStackTrace();
            }
        }
        return fileNames;
    }


    private class FileFilter implements DirectoryStream.Filter<Path> {

        private String extension;


        FileFilter(String extension) {
            this.extension = extension.toLowerCase();
        }


        @Override
        public boolean accept(Path entry) throws IOException {
            return entry.toString().toLowerCase().endsWith('.' + extension);
        }
    }
}

