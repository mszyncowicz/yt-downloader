package api;

import java.io.File;
import java.io.Reader;
import java.util.Optional;

@FunctionalInterface
public interface FileLocator extends Locator{
    Optional<File> locate(Reader reader, String directory);
}
