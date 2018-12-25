package api;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface LinkLocator extends Locator {
    Map<String,URL> locate(Reader reader, String directory);

    enum Keys{
        BEST_MP4,
        BEST_VIDEO,
        BEST_AUDIO;
    }
}
