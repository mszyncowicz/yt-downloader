import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class YtDownloaderTest {

    @Test
    @Ignore
    public void blokEkipaTest() throws IOException {
        String url = "https://www.youtube.com/watch?v=_Yhyp-_hX2s";

        YtDownloader downloader = new YtDownloader(url);

        List<String> urls = downloader.getURLs();

        Assert.assertFalse(urls.isEmpty());

        Assert.assertNotNull(urls);

        for (String resultUrl : urls){
            Assert.assertTrue(resultUrl.startsWith("http"));
            String urlMatcher = "http[s]{0,1}:\\/\\/[www\\.]{0,1}[\\d\\w\\.]+[\\/\\d\\w\\?=\\D\\W]+";
            Assert.assertTrue(resultUrl.matches(urlMatcher));
        }

    }
}
