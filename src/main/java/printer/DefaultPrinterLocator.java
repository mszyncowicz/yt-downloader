package printer;

import api.LinkLocator;
import api.Locator;
import com.fasterxml.jackson.databind.ObjectMapper;
import downloader.DefaultFileLocator;

import java.io.Reader;
import java.net.URL;
import java.util.Map;

public class DefaultPrinterLocator implements LinkLocator {

    public static DefaultPrinterLocator defaultPrinterLocator = new DefaultPrinterLocator();

    private static ObjectMapper mapper = new ObjectMapper();

    private DefaultPrinterLocator(){

    }
    @Override
    public Map<String, URL> locate(Reader reader, String directory) {
        return null;
    }
}
