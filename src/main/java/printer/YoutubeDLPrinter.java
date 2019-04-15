package printer;

import api.LinkPrinter;
import downloader.WrongParametersException;
import downloader.YoutubeDLCommander;

import java.util.Map;

public class YoutubeDLPrinter implements LinkPrinter {
    private static final String PRINT_JSON = "--print-json --skip-download";

    @Override
    public String getCommand(Map<String, String> args) throws WrongParametersException {
        return "youtube-dl " + args.get(Args.url.name()) + " " + PRINT_JSON;
    }

    @Override
    public String printBestVideo() {
        return null;
    }

    @Override
    public String printBestAudio() {
        return null;
    }

    @Override
    public String printBestAudioVideo() {
        return null;
    }

    public static enum Args{
        url;
    }
}
