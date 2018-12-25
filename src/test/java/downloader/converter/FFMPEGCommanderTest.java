package downloader.converter;

import api.Commander;
import downloader.MediaToolExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FFMPEGCommanderTest {

    @Test
    public void shouldConvertFromOneFormatToAnother(){

        File fileOne = new File("name1");

        Commander commander = FFMPEGCommander.convertFromatToFormat(fileOne.getName(), "mp4");

        Map<String, String> stringStringMap = new HashMap<>();
        MediaToolExecutor mediaToolExecutor = MediaToolExecutor.createExecutor(commander, stringStringMap);

    }

    @Test
    public void shouldMergeAudioAndVideoStreams(){

        File fileOne = new File("name1");
        File fileTwo = new File("name2");
        Commander commander = FFMPEGCommander.mergeVideoAndAudio(fileOne.getName(), fileTwo.getName(),"mp4");

        Map<String, String> stringStringMap = new HashMap<>();
        MediaToolExecutor mediaToolExecutor = MediaToolExecutor.createExecutor(commander, stringStringMap);

    }
}