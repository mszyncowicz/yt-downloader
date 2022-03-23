package downloader;

import api.Commander;
import lombok.Builder;
import lombok.Getter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Builder
public class YoutubeDLCommander implements Commander {
    public static final String URL_PARAM = "url";
    public static final String AUDIO_FORMAT_PARAM = "audio_format";
    public static final String OUTPUT_PARAM = "output";
    public static final String TIME_PARAM = "time";
    public static final String UUID_PARAM = "uuid";

    private static final String SKIP_DOWNLOAD = "--skip-download ";
    private static final String LIST_FORMATS = "--list-formats ";
    private static final String BEST_VIDEO = "-f \"bestvideo\" --recode-video mp4 --postprocessor-args \"-c:v nvenc" +
            " -rc constqp -qp 23 -preset llhq ?PA\" ";
    private static final String BEST_AUDIO = "--extract-audio --audio-format mp3 --audio-quality 0 --postprocessor-args \"?PA\" ";
    private static final String BEST_AUDIOVIDEO = " ";
    private boolean skipDownload;
    private boolean printJson;
    private boolean listFormats;
    private boolean bestVideo;
    private boolean bestAudio;

    String postargs;

    @Getter
    String[] required;

    private void setRequired(String[] required) {
        this.required = required;
    }

    public static YoutubeDLCommander getBestVideo() {
        String[] required = {URL_PARAM, UUID_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(true)
                .bestAudio(false)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    public static YoutubeDLCommander getBestAudioVideo() {
        String[] required = {URL_PARAM, UUID_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(true)
                .bestAudio(true)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    public static YoutubeDLCommander getBestAudio() {
        String[] required = {URL_PARAM, UUID_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(false)
                .bestAudio(true)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    static YoutubeDLCommander printJSON() {
        String[] required = {URL_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.printJson(true)
                .skipDownload(true)
                .listFormats(false)
                .bestVideo(false)
                .bestAudio(false)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    public String getCommand(Map<String, String> args) {
        Commander.super.argsCheck(args, required);
        StringBuffer buffer = new StringBuffer();
        buffer.append("yt-dlp ");
        postargs = "";
        String source = args.get(URL_PARAM);
        if (source == null) throw new IllegalArgumentException("No url in arguments");
        if (skipDownload) buffer.append(SKIP_DOWNLOAD);
        if (bestVideo && bestAudio)
        {
            buffer.append(BEST_AUDIOVIDEO);
        } else {
            if (bestAudio) {
                String bestAudioFormat = BEST_AUDIO;
                String format = args.get(AUDIO_FORMAT_PARAM);
                if (format != null) {
                    bestAudioFormat.replace("mp3", format);
                }
                buffer.append(bestAudioFormat);
            }
            else buffer.append(BEST_VIDEO);
        }
        if (listFormats) buffer.append(LIST_FORMATS);
        setTime(args);
        buffer.append(source + " ");
        String output = args.get(OUTPUT_PARAM);
        if (output != null) {
            buffer.append("-o " + output);
        }
        return buffer.toString().replace("?PA", postargs);
    }

    private void setTime(Map<String, String> args) {
        if (args.containsKey(TIME_PARAM)) {
            String timeArg = args.get(TIME_PARAM);
            postargs = postargs + timeArg;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YoutubeDLCommander that = (YoutubeDLCommander) o;
        return skipDownload == that.skipDownload &&
                printJson == that.printJson &&
                listFormats == that.listFormats &&
                bestVideo == that.bestVideo &&
                bestAudio == that.bestAudio &&
                Objects.equals(postargs,that.postargs) &&
                Arrays.equals(required, that.required);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(skipDownload, printJson, listFormats, bestVideo, bestAudio);
        result = 31 * result + Arrays.hashCode(required);
        return result;
    }
}
