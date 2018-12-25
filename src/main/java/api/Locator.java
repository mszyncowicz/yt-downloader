package api;

import java.io.File;
import java.io.Reader;
import java.util.Optional;

@FunctionalInterface
public interface Locator {
    Object locate(Reader reader, String directory);
}
