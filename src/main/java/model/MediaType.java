package model;


import downloader.YoutubeDLCommander;
import lombok.Getter;

public enum  MediaType {
    audio(YoutubeDLCommander.getBestAudio()),
    video(YoutubeDLCommander.getBestVideo()),
    audiovideo(YoutubeDLCommander.getBestAudioVideo());

    @Getter
    private YoutubeDLCommander commander;

    MediaType(YoutubeDLCommander commander)
    {
        this.commander = commander;
    }

}
