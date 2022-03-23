package downloader;

import api.Commander;

import java.util.Map;

public class FFMPEGCommander implements Commander {
    public static final String OUTPUT_PARAM = "#output#";
    public static final String INPUT_PARAM = "#input#";
    public static final String SPLIT = "#SPLIT#";
    private static final String CONVERTER_OPTIONS = "ffmpeg" + SPLIT
            + "-i" + SPLIT
            + INPUT_PARAM + SPLIT
            + "-c:v" + SPLIT +"nvenc" + SPLIT
            + "-rc" + SPLIT + "constqp" + SPLIT + "-qp" + SPLIT + "23" + SPLIT + "-preset" + SPLIT + "llhq"
            + SPLIT + OUTPUT_PARAM;

    @Override
    public String getCommand(Map<String, String> args) throws WrongParametersException {
        return CONVERTER_OPTIONS.replace(INPUT_PARAM, args.get(INPUT_PARAM)).replace(OUTPUT_PARAM, args.get(OUTPUT_PARAM));
    }
}
