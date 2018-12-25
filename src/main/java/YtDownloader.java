import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class YtDownloader {

    private String url;

    public YtDownloader(String url){
        this.url = url;
    }

    public static Document parseUrl(String url) throws IOException {
        return Jsoup.connect(url).execute().parse();
    }

    public List<String> getURLs() throws MalformedURLException {
        List<String> urls = new ArrayList<String>();
        Document source;
        try {
            source = parseUrl(url);
        } catch (IOException e) {
           return urls;
        }
        Elements script = source.select("script");
        Element element = null;
        for (Element e : script){
            if (e.data().contains("var ytplayer = ytplayer")) {
                element = e;
                break;
            }
        }

        if (element == null) {
            System.out.println("didnt find");
            return urls;
        }
        else {
            System.out.println(element.data());
            String data = element.data();




            String[] split = data.split("url=");
            for (String s : split){
                if(s.contains("googlevideo")) {
                    try {
                        urls.add(URLDecoder.decode(s.substring(0,s.indexOf("\\")), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {

                    }
                }
            }

            for(String ur : urls){
               // System.out.println(ur);
                Uri uri = new Uri(ur);

                System.out.println(uri.toString());
            }

            // url = url.substring(0, url.indexOf("\\"));


            Uri uri = new Uri(url);

            System.out.println(uri.toString());


        }

        return urls;
    }
}
