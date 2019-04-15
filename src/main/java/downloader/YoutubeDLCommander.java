package downloader;

import api.Commander;
import lombok.Builder;
import lombok.Getter;
import org.apache.activemq.artemis.utils.UUID;

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

    private boolean skipDownload = false;
    private boolean printJson = false;
    private boolean listFormats = false;
    private boolean bestVideo = false;
    private boolean bestAudio = false;
    private boolean isTimed = false;

    @Getter
    String[] required = {};

    private void setRequired(String[] required) {
        this.required = required;
    }

    public static YoutubeDLCommander getBestVideo(){
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
    public static YoutubeDLCommander getBestVideoTimed(){
        String[] required = {URL_PARAM, UUID_PARAM,TIME_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(true)
                .bestAudio(false)
                .isTimed(true)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }
    public static YoutubeDLCommander getBestAudio(){
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
    public static YoutubeDLCommander getBestAudioTimed(){
        String[] required = {URL_PARAM, UUID_PARAM,TIME_PARAM};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(false)
                .bestAudio(true)
                .isTimed(true)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }
    public static YoutubeDLCommander printJSON(){
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

    public String getCommand(Map<String,String> args){
        Commander.super.argsCheck(args,required);
        StringBuffer buffer = new StringBuffer();
        buffer.append("youtube-dl ");
        String source = args.get(URL_PARAM);
        if (source == null) throw new IllegalArgumentException("No url in arguments");
        if (skipDownload) buffer.append(SKIP_DOWNLOAD);
        if (bestAudio){
            String bestAudioFormat = BEST_AUDIO;
            String format = args.get(AUDIO_FORMAT_PARAM);
            if (format != null){
                bestAudioFormat.replace("mp3", format);
            }
            buffer.append(bestAudioFormat);
        }
        if (bestVideo) buffer.append(BEST_VIDEO);
        if (listFormats)buffer.append(LIST_FORMATS);
        String postArgs = "";
        if (isTimed) {
            String timeArg = args.get(TIME_PARAM);
            postArgs = postArgs + timeArg;
        }
        buffer.append(source + " ");
        String output = args.get(OUTPUT_PARAM);
        if (output != null){
            buffer.append("-o " + output);
        }
        return buffer.toString().replace("?PA",postArgs);
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
                isTimed == that.isTimed &&
                Arrays.equals(required, that.required);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(skipDownload, printJson, listFormats, bestVideo, bestAudio, isTimed);
        result = 31 * result + Arrays.hashCode(required);
        return result;
    }
}
