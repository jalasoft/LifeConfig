package cz.jalasoft.lifeconfig.source;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cz.jalasoft.lifeconfig.util.ArgumentAssertion.*;
import static java.nio.file.Files.*;

/**
 * A configuration source reading data from
 * a filesystem.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public final class FileConfigSource implements ConfigSource {

    private final Path file;

    public FileConfigSource(Path file) {
        mustNotBeNull(file, "Resource file");
        mustBeTrue(isRegularFile(file), "File path " + file + " is a regular file.");

        this.file = file;
    }

    public FileConfigSource(String file) {
        this(Paths.get(file));
    }

    @Override
    public Reader load() throws IOException {
        Reader result = newBufferedReader(file, Charset.forName("UTF-8"));
        return result;
    }

    @Override
    public String name() {
        return file.toString();
    }

    @Override
    public long lastModifiedMillis() throws IOException {
        return getLastModifiedTime(file).toMillis();
    }
}
