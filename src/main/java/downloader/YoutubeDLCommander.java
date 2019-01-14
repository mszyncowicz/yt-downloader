package downloader;

import api.Commander;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
public class YoutubeDLCommander implements Commander {
    private static final String SKIP_DOWNLOAD = "--skip-download ";
    private static final String LIST_FORMATS = "--list-formats ";
    private static final String BEST_VIDEO = "--recode-video mp4 --postprocessor-args \"-c:v libx264\" ";
    private static final String BEST_AUDIO = "--extract-audio --audio-format mp3 --audio-quality 0 ";

    private boolean skipDownload = false;
    private boolean printJson = false;
    private boolean listFormats = false;
    private boolean bestVideo = false;
    private boolean bestAudio = false;

    @Getter
    String[] required = {};

    private void setRequired(String[] required) {
        this.required = required;
    }

    public static YoutubeDLCommander getBestVideo(){
        String[] required = {Args.url.name(), Args.uuid.name()};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(true)
                .bestAudio(false)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    public static YoutubeDLCommander getBestAudio(){
        String[] required = {Args.url.name(), Args.uuid.name()};
        YoutubeDLCommanderBuilder youtubeDLCommanderBuilder = new YoutubeDLCommanderBuilder();
        youtubeDLCommanderBuilder.skipDownload(false)
                .printJson(false)
                .listFormats(false)
                .bestVideo(false)
                .bestAudio(true)
                .required(required);
        return youtubeDLCommanderBuilder.build();
    }

    public String getCommand(Map<String,String> args){
        Commander.super.argsCheck(args,required);
        StringBuffer buffer = new StringBuffer();
        buffer.append("youtube-dl ");
        String source = args.get("url");
        if (source == null) throw new IllegalArgumentException("No url in arguments");
        if (skipDownload) buffer.append(SKIP_DOWNLOAD);
        if (bestAudio){
            String bestAudioFormat = BEST_AUDIO;
            String format = args.get("audio_format");
            if (format != null){
                bestAudioFormat.replace("mp3", format);
            }
            buffer.append(bestAudioFormat);
        }
        if (bestVideo) buffer.append(BEST_VIDEO);
        if (listFormats)buffer.append(LIST_FORMATS);
        buffer.append(source + " ");
        String output = args.get("output");
        if (output != null){
            buffer.append("-o " + output);
        }
        return buffer.toString();
    }

    public static enum Args{
        uuid,
        output,
        url,
        audio_format;
    }
}
