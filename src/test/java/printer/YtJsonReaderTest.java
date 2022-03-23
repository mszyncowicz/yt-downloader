package printer;

import dto.YtMediaDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YtJsonReaderTest {

    private static final String RESULT_JSON = "{\"thumbnail\": \"https://i.ytimg.com/vi/YO8c7CjmS_E/maxresdefault.jpg\", \"webpage_url\": \"https://www.youtube.com/watch?v=YO8c7CjmS_E\", \"tags\": [\"\\u041f\\u0430\\u0439\\u043d\\u0435\\u0440\", \"Payner\", \"Planeta 4K\", \"PlanetaOfficial\", \"planetahdtv\", \"4K Ultra HD\", \"\\u043c\\u0443\\u0437\\u0438\\u043a\\u0430\", \"music\", \"Music Video (Musical Genre)\", \"Bulgarian Music (Musical Genre)\", \"\\u0420\\u0410\\u0419\\u041d\\u0410\", \"RAYNA\", \"\\u0417\\u043b\\u0435 \\u0442\\u0435 \\u0447\\u0443\\u0432\\u0430\\u043c\", \"Zle te chuvam\", \"2018\", \"RAINA\"], \"playlist\": null, \"height\": 2160, \"abr\": 160, \"license\": null, \"automatic_captions\": {}, \"playlist_index\": null, \"categories\": [\"Entertainment\"], \"requested_subtitles\": null, \"formats\": [{\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"tbr\": 55.456, \"filesize\": 1240522, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"format\": \"249 - audio only (DASH audio)\", \"abr\": 50, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5511222&clen=1240522&mime=audio%2Fwebm&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=249&gir=yes&dur=196.661&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540196513679718&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=480CB395AFD3945B55776DA56B8F90577E271D11.7B9FAFE35C9B8EDF4D9D14BA78E0808BEF873C76&ratebypass=yes\", \"vcodec\": \"none\", \"acodec\": \"opus\", \"format_note\": \"DASH audio\", \"ext\": \"webm\", \"format_id\": \"249\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"tbr\": 72.095, \"filesize\": 1627263, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"format\": \"250 - audio only (DASH audio)\", \"abr\": 70, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5511222&clen=1627263&mime=audio%2Fwebm&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=250&gir=yes&dur=196.661&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540196513467678&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=5B61715E648BD011D4A214D351762E501B882AEF.3815CB809CF05E8BBF8166B46C6D1D87A21B373A&ratebypass=yes\", \"vcodec\": \"none\", \"acodec\": \"opus\", \"format_note\": \"DASH audio\", \"ext\": \"webm\", \"format_id\": \"250\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"quality\": -1, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"acodec\": \"mp4a.40.2\", \"tbr\": 128.064, \"abr\": 128, \"protocol\": \"https\", \"filesize\": 3124923, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"format\": \"140 - audio only (DASH audio)\", \"format_note\": \"DASH audio\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=3124923&mime=audio%2Fmp4&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=140&gir=yes&dur=196.696&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195336841244&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=7F6AC0B3D2F2CA30FEC2D29DC69FD010BFA07185.5BF4275BD2CABE0D493B04FB4A043A5E58D82352&ratebypass=yes\", \"vcodec\": \"none\", \"container\": \"m4a_dash\", \"format_id\": \"140\", \"ext\": \"m4a\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"tbr\": 140.493, \"filesize\": 3162960, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"format\": \"171 - audio only (DASH audio)\", \"abr\": 128, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5511222&clen=3162960&mime=audio%2Fwebm&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=171&gir=yes&dur=196.636&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540196513412095&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=7425E0113DFDF7B85F6532FA3DF2F5E8A00020EC.60552C42F1D657D13E65BB543008A9B7946CC91A&ratebypass=yes\", \"vcodec\": \"none\", \"acodec\": \"vorbis\", \"format_note\": \"DASH audio\", \"ext\": \"webm\", \"format_id\": \"171\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"tbr\": 141.616, \"filesize\": 3238041, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"format\": \"251 - audio only (DASH audio)\", \"abr\": 160, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5511222&clen=3238041&mime=audio%2Fwebm&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=251&gir=yes&dur=196.661&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540196513466812&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=63B44AE81BCCAEAEDEEAB1EF596AE0945FD6CF11.33911090356C8ADD55D962B068AED278237A8640&ratebypass=yes\", \"vcodec\": \"none\", \"acodec\": \"opus\", \"format_note\": \"DASH audio\", \"ext\": \"webm\", \"format_id\": \"251\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"acodec\": \"none\", \"tbr\": 102.635, \"filesize\": 2085850, \"width\": 256, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 144, \"format_note\": \"144p\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=2085850&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=278&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195720302642&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=C43A03DAC62DF46CC84FC0233AF82622DA95B4C2.99D7A1F3183080AB0256C080AFFB594A52301410&ratebypass=yes\", \"format\": \"278 - 256x144 (144p)\", \"vcodec\": \"vp9\", \"container\": \"webm\", \"protocol\": \"https\", \"format_id\": \"278\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 112.763, \"filesize\": 1956933, \"width\": 256, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 144, \"format_id\": \"160\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=1956933&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=160&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195373760034&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=15B999C94AED58E71A35C3A2E34C965829CE6CA2.CBB18725B94F1080D590046B3E095BAB7F8F503E&ratebypass=yes\", \"format\": \"160 - 256x144 (144p)\", \"vcodec\": \"avc1.4d400c\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"144p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 233.564, \"filesize\": 4591458, \"width\": 426, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 240, \"format_id\": \"242\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=4591458&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=242&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195720295141&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=AFDE2BBB567914D66D017F7271BCD63B60DC31E7.592491B2280CC1D64833AC4916691FD8BAC10D5F&ratebypass=yes\", \"format\": \"242 - 426x240 (240p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"240p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 248.675, \"filesize\": 3765858, \"width\": 426, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 240, \"format_id\": \"133\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=3765858&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=133&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195373759656&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=7620FD3948177A1521BB24A2960E64507E98CBB5.652778A51B622CF82CD1E8D6FBACC96B1F203FC6&ratebypass=yes\", \"format\": \"133 - 426x240 (240p)\", \"vcodec\": \"avc1.4d4015\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"240p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 428.344, \"filesize\": 8359170, \"width\": 640, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 360, \"format_id\": \"243\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=8359170&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=243&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195720292942&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=4F6C4B148991C7F8C77F28419FF7075AF26E498E.A2B270DBED83F1ADD37D906663155B6C6428F30E&ratebypass=yes\", \"format\": \"243 - 640x360 (360p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"360p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 637.354, \"filesize\": 9200708, \"width\": 640, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 360, \"format_id\": \"134\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=9200708&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=134&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195373776983&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=D7B27DB62AD649C5C80EA2ED873C6A81703A456B.9FF2F97E0BAF56BEF40A43A8AED7FE05D939BDF1&ratebypass=yes\", \"format\": \"134 - 640x360 (360p)\", \"vcodec\": \"avc1.4d401e\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"360p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 761.091, \"filesize\": 15074142, \"width\": 854, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 480, \"format_id\": \"244\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=15074142&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=244&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195720296101&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=C40E9A1915DAB4AA6E6C313E47F3C6789FDBEEDD.78461938AEC0D4AE64C2CA804422FBB8BF438625&ratebypass=yes\", \"format\": \"244 - 854x480 (480p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"480p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 1166.811, \"filesize\": 17715238, \"width\": 854, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 480, \"format_id\": \"135\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=17715238&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=135&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195373776095&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=A1CA60A63F40CCA7661058688EBFF5B5AA1FBFD6.B73D8BB94C4B3E78B210B94D5226D8E763C7FA7A&ratebypass=yes\", \"format\": \"135 - 854x480 (480p)\", \"vcodec\": \"avc1.4d401e\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"480p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 1680.373, \"filesize\": 31232802, \"width\": 1280, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 720, \"format_id\": \"247\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=31232802&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=247&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195720292075&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=B02AF29C31E49A348E3FEB1CF51467B8DD4683AD.04F67DA1E198822CDC22E5807470483398A8B990&ratebypass=yes\", \"format\": \"247 - 1280x720 (720p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"720p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 2339.104, \"filesize\": 32733110, \"width\": 1280, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 720, \"format_id\": \"136\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=32733110&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=136&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195424110877&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=59373F00119443296C8D734051999EDF64066597.30E83AE31B675FB1387A1EB43BE82E1CCBAC32FF&ratebypass=yes\", \"format\": \"136 - 1280x720 (720p)\", \"vcodec\": \"avc1.4d401f\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"720p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 2899.607, \"filesize\": 54232228, \"width\": 1920, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 1080, \"format_id\": \"248\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=54232228&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=248&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195512503083&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=1702DBA8AF92076EA37F917DB6D47C7AE89D0347.8DEC5D8D675CCC308E100D5FC51682D7C34C66D5&ratebypass=yes\", \"format\": \"248 - 1920x1080 (1080p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"1080p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 3833.49, \"filesize\": 55261968, \"width\": 1920, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 1080, \"format_id\": \"137\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=55261968&mime=video%2Fmp4&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=137&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195393882438&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=CB491584E1563A7AAAF34B3B2CA077532E9C2422.36E79C6AF7DEBE72A0B0487B4C8756DD1F3318A0&ratebypass=yes\", \"format\": \"137 - 1920x1080 (1080p)\", \"vcodec\": \"avc1.640028\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"1080p\", \"ext\": \"mp4\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 8241.539, \"filesize\": 139692166, \"width\": 2560, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 1440, \"format_id\": \"271\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=139692166&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=271&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195604196044&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=A3C8416E52B885A522AD65A2DDA56302B351B3B1.7409EC82F35282CCF9F32D0B38E4954D88E8F109&ratebypass=yes\", \"format\": \"271 - 2560x1440 (1440p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"1440p\", \"ext\": \"webm\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 17734.903, \"filesize\": 329301787, \"width\": 3840, \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 2160, \"format_id\": \"313\", \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=329301787&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=313&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195813199567&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=CCD25819E5347E6C29A787C5FEF87D32F0FF6833.9DF1B6C98E4A1BCAE951F11671DD84CB3783A0CA&ratebypass=yes\", \"format\": \"313 - 3840x2160 (2160p)\", \"vcodec\": \"vp9\", \"acodec\": \"none\", \"protocol\": \"https\", \"format_note\": \"2160p\", \"ext\": \"webm\"}, {\"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": null, \"tbr\": 581.765, \"filesize\": 14303861, \"asr\": 44100, \"width\": 640, \"quality\": 1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 360, \"abr\": 96, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&ratebypass=yes&txp=5531432&clen=14303861&mime=video%2Fmp4&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=18&gir=yes&dur=196.696&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195388465974&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=BB4ED6DAB0CD7D94ED6C06D1C2B260E80F16F4C2.0B1DED99F275C6E7339AC0B0A38EAEE38DDA64B6\", \"format\": \"18 - 640x360 (medium)\", \"vcodec\": \"avc1.42001E\", \"acodec\": \"mp4a.40.2\", \"protocol\": \"https\", \"format_id\": \"18\", \"ext\": \"mp4\", \"format_note\": \"medium\"}], \"_filename\": \"RAYNA - ZLE TE CHUVAM _ \\u0420\\u0430\\u0439\\u043d\\u0430 - \\u0417\\u043b\\u0435 \\u0442\\u0435 \\u0447\\u0443\\u0432\\u0430\\u043c, 2018-YO8c7CjmS_E.webm\", \"is_live\": null, \"fps\": 25, \"view_count\": 6015714, \"stretched_ratio\": null, \"extractor_key\": \"Youtube\", \"annotations\": null, \"fulltitle\": \"RAYNA - ZLE TE CHUVAM / \\u0420\\u0430\\u0439\\u043d\\u0430 - \\u0417\\u043b\\u0435 \\u0442\\u0435 \\u0447\\u0443\\u0432\\u0430\\u043c, 2018\", \"subtitles\": {}, \"vbr\": null, \"start_time\": null, \"episode_number\": null, \"dislike_count\": 2382, \"thumbnails\": [{\"url\": \"https://i.ytimg.com/vi/YO8c7CjmS_E/maxresdefault.jpg\", \"id\": \"0\"}], \"uploader\": \"PlanetaOfficial\", \"alt_title\": \"ZLE TE CHUVAM 2017\", \"extractor\": \"youtube\", \"format\": \"313 - 3840x2160 (2160p)+251 - audio only (DASH audio)\", \"uploader_url\": \"http://www.youtube.com/user/PlanetaOfficial\", \"average_rating\": null, \"track\": \"ZLE TE CHUVAM 2017\", \"upload_date\": \"20180108\", \"resolution\": null, \"vcodec\": \"vp9\", \"format_id\": \"313+251\", \"ext\": \"webm\", \"requested_formats\": [{\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"fps\": 25, \"tbr\": 17734.903, \"filesize\": 329301787, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"height\": 2160, \"width\": 3840, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=aitags%2Cclen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5532432&clen=329301787&mime=video%2Fwebm&fvip=4&expire=1552276297&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=313&gir=yes&dur=196.640&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540195813199567&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&ipbits=0&signature=CCD25819E5347E6C29A787C5FEF87D32F0FF6833.9DF1B6C98E4A1BCAE951F11671DD84CB3783A0CA&ratebypass=yes\", \"format\": \"313 - 3840x2160 (2160p)\", \"acodec\": \"none\", \"format_id\": \"313\", \"ext\": \"webm\", \"format_note\": \"2160p\", \"vcodec\": \"vp9\"}, {\"downloader_options\": {\"http_chunk_size\": 10485760}, \"http_headers\": {\"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\", \"User-Agent\": \"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\", \"Accept-Encoding\": \"gzip, deflate\", \"Accept-Language\": \"en-us,en;q=0.5\", \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.7\"}, \"tbr\": 141.616, \"filesize\": 3238041, \"protocol\": \"https\", \"quality\": -1, \"player_url\": \"/yts/jsbin/player_ias-vflca9-f7/en_US/base.js\", \"abr\": 160, \"url\": \"https://r6---sn-f5f7ln7e.googlevideo.com/videoplayback?keepalive=yes&c=WEB&key=yt6&pl=13&initcwndbps=1428750&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Crequiressl%2Csource%2Cexpire&source=youtube&requiressl=yes&txp=5511222&clen=3238041&mime=audio%2Fwebm&fvip=4&ipbits=0&mn=sn-f5f7ln7e%2Csn-4g5e6nsy&mm=31%2C26&itag=251&gir=yes&dur=196.661&mv=m&mt=1552254588&ms=au%2Conr&lmt=1540196513466812&ip=89.70.36.181&ei=6IaFXLaWOZXsyQW5uKiwDA&expire=1552276297&id=o-AArYdVsg--98heluNpDHEOz176KHebL5EgTwcKCzXeXb&signature=63B44AE81BCCAEAEDEEAB1EF596AE0945FD6CF11.33911090356C8ADD55D962B068AED278237A8640&ratebypass=yes\", \"format\": \"251 - audio only (DASH audio)\", \"acodec\": \"opus\", \"format_note\": \"DASH audio\", \"ext\": \"webm\", \"format_id\": \"251\", \"vcodec\": \"none\"}], \"description\": \"Producer: Payner Media Ltd. / Payner Ltd.\\nMedia: Planeta TV, Planeta HD, Planeta 4K\\nAll rights reserved :\\n(C) & (P) Payner Ltd., Bulgaria\\n\\u0417\\u0430 \\u043f\\u0443\\u0431\\u043b\\u0438\\u0447\\u043d\\u0438 \\u0438\\u0437\\u044f\\u0432\\u0438 \\u0438 \\u0443\\u0447\\u0430\\u0441\\u0442\\u0438\\u044f: tel: 0391 603 27, 088 8 726 298, 087 9 000 006\\nmail: office@payner.bg\\nBulgaria, 6400 Dimitrovgrad, bul. Hristo Botev 16\\n\\nweb: http://payner.bg\\nFacebook: https://www.facebook.com/pages/Planeta-TV-%D0%A2%D0%B5%D0%BB%D0%B5%D0%B2%D0%B8%D0%B7%D0%B8%D1%8F-%D0%9F%D0%BB%D0%B0%D0%BD%D0%B5%D1%82%D0%B0/223168207727156\\ntwitter:  https://twitter.com/PlanetaHDTV\\ninstagram: https://www.instagram.com/planetahdtv/\\n\\n\\u043f\\u0440\\u043e\\u0434\\u0443\\u0446\\u0435\\u043d\\u0442: \\u041f\\u0410\\u0419\\u041d\\u0415\\u0420 \\u041e\\u041e\\u0414\\n\\u043c\\u0443\\u0437\\u0438\\u043a\\u0430 \\u0438 \\u0430\\u0440\\u0430\\u043d\\u0436\\u0438\\u043c\\u0435\\u043d\\u0442: \\u041c\\u0430\\u0440\\u0442\\u0438\\u043d \\u0411\\u0438\\u043e\\u043b\\u0447\\u0435\\u0432\\n\\u0442\\u0435\\u043a\\u0441\\u0442: \\u0410\\u043d\\u0430\\u0441\\u0442\\u0430\\u0441\\u0438\\u044f \\u041c\\u0430\\u0432\\u0440\\u043e\\u0434\\u0438\\u0435\\u0432\\u0430 \\u0438 \\u0420\\u043e\\u0441\\u0435\\u043d \\u0414\\u0438\\u043c\\u0438\\u0442\\u0440\\u043e\\u0432\\n\\u0440\\u0435\\u0436\\u0438\\u0441\\u044c\\u043e\\u0440 \\u0438 \\u0441\\u0446\\u0435\\u043d\\u0430\\u0440\\u0438\\u0441\\u0442: \\u041b\\u044e\\u0434\\u043c\\u0438\\u043b \\u0418\\u043b\\u0430\\u0440\\u0438\\u043e\\u043d\\u043e\\u0432 - \\u041b\\u044e\\u0441\\u0438\\n\\u043e\\u043f\\u0435\\u0440\\u0430\\u0442\\u043e\\u0440: \\u0410\\u043d\\u0434\\u0440\\u0435\\u0439 \\u0410\\u043d\\u0434\\u0440\\u0435\\u0435\\u0432\\n\\nOfficial Download Links:\\nDeezer: https://www.deezer.com/album/57661962\\niTunes/Apple: https://itunes.apple.com/album/id1351994133\\nSpotify: https://open.spotify.com/album/2BWkAgfMTfy8PaOeCxpmXh\\nGooglePlay: https://play.google.com/store/music/album/%D0%A0%D0%B0%D0%B9%D0%BD%D0%B0_%D0%97%D0%BB%D0%B5_%D1%82%D0%B5_%D1%87%D1%83%D0%B2%D0%B0%D0%BC?id=Bfmgciugqg35i75dqdseeiklium\\nAmazon: https://www.amazon.com/%D0%97%D0%BB%D0%B5-%D1%82%D0%B5-%D1%87%D1%83%D0%B2%D0%B0%D0%BC-%D0%A0%D0%B0%D0%B9%D0%BD%D0%B0/dp/B079ZZMNX7/ref=sr_1_1?s=dmusic&ie=UTF8&qid=1548243724&sr=1-1-mp3-albums-bar-strip-0&keywords=%D0%B7%D0%BB%D0%B5+%D1%82%D0%B5+%D1%87%D1%83%D0%B2%D0%B0%D0%BC\", \"season_number\": null, \"channel_url\": \"http://www.youtube.com/channel/UCffdZGxiVRy3i9EaZF4m0wA\", \"uploader_id\": \"PlanetaOfficial\", \"creator\": \"RAYNA\", \"channel_id\": \"UCffdZGxiVRy3i9EaZF4m0wA\", \"end_time\": null, \"id\": \"YO8c7CjmS_E\", \"display_id\": \"YO8c7CjmS_E\", \"age_limit\": 0, \"like_count\": 32528, \"width\": 3840, \"acodec\": \"opus\", \"artist\": \"RAYNA\", \"webpage_url_basename\": \"watch\", \"duration\": 197, \"chapters\": null, \"series\": null, \"title\": \"RAYNA - ZLE TE CHUVAM / \\u0420\\u0430\\u0439\\u043d\\u0430 - \\u0417\\u043b\\u0435 \\u0442\\u0435 \\u0447\\u0443\\u0432\\u0430\\u043c, 2018\"}";

    YtMediaDetails result;

    @BeforeEach
    public void init(){
        YtJsonReader ytJsonReader = new YtJsonReader();
        result = ytJsonReader.getDataFrom(RESULT_JSON);
    }

    @Test
    public void duration(){
        Assertions.assertNotNull(result.getLength());
        Assertions.assertEquals("197",result.getLength());
    }

    @Test
    public void thumbnail(){
        Assertions.assertNotNull(result.getThumbnail());
        Assertions.assertEquals("https://i.ytimg.com/vi/YO8c7CjmS_E/maxresdefault.jpg",result.getThumbnail());
    }

    @Test
    public void title(){
        Assertions.assertNotNull(result.getTitle());
        String expected = "RAYNA - ZLE TE CHUVAM / Райна - Зле те чувам, 2018";
        Assertions.assertEquals(expected,result.getTitle());
    }

    @Test
    public void author(){
        Assertions.assertNotNull(result.getAuthor());
        Assertions.assertEquals("PlanetaOfficial",result.getAuthor());
    }
}
