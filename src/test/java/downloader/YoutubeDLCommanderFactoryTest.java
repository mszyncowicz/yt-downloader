package downloader;

import dto.YTParametersDTO;
import model.MediaType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class YoutubeDLCommanderFactoryTest {
    
    private YoutubeDLCommanderFactoryInterface factoryInterface = YoutubeCommanderFactory.INSTANCE;

    @Test
    public void shouldReturnTimedAudio(){
        String mediaType = MediaType.audio.name();
        YTParametersDTO parametersDTO = getTimedYtParametersDTO(mediaType,"12","13");
        HashMap<String, String> parameters = new HashMap<>();

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, parameters);

        Assert.assertEquals(command,YoutubeDLCommander.getBestAudio());
        String shouldReturn = "-ss 00:00:12 -t 00:00:01";
        Assert.assertEquals(shouldReturn,parameters.get(YoutubeDLCommander.TIME_PARAM));
    }

    @Test
    public void shouldNotReturnTimedAudioIfPassedTimesAreNotInSeconds(){
        String mediaType = MediaType.audio.name();
        YTParametersDTO parametersDTO = getTimedYtParametersDTO(mediaType,"00:12:12","13");
        HashMap<String, String> parameters = new HashMap<>();

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, parameters);

        assertNotTImed(parameters, command);
    }

    @Test
    public void shouldNotReturnTimedAudioIfPassedTimeFromIsNull(){
        String mediaType = MediaType.audio.name();
        YTParametersDTO parametersDTO = getTimedYtParametersDTO(mediaType,null,"13");
        HashMap<String, String> parameters = new HashMap<>();

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, parameters);

        assertNotTImed(parameters, command);
    }

    @Test
    public void shouldNotReturnTimedAudioIfPassedTimeToIsNull(){
        String mediaType = MediaType.audio.name();
        YTParametersDTO parametersDTO = getTimedYtParametersDTO(mediaType,"12",null);
        HashMap<String, String> parameters = new HashMap<>();

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, parameters);

        assertNotTImed(parameters, command);
    }

    private void assertNotTImed(HashMap<String, String> parameters, YoutubeDLCommander command) {
        Assert.assertEquals(command, YoutubeDLCommander.getBestAudio());
        Assert.assertNull(parameters.get(YoutubeDLCommander.TIME_PARAM));
    }

    private YTParametersDTO getTimedYtParametersDTO(String mediaType, String timeFrom, String timeTo) {
        YTParametersDTO parametersDTO = new YTParametersDTO();
        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom(timeFrom);
        parametersDTO.setTimeTo(timeTo);
        return parametersDTO;
    }

    @Test
    public void shouldReturnAudio(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.audio.name();

        parametersDTO.setMediaType(mediaType);

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, new HashMap<>());
        Assert.assertEquals(YoutubeDLCommander.getBestAudio(),command);
    }
    @Test
    public void shouldReturnAudio2(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.audio.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("00:00:12");
        parametersDTO.setTimeTo("00:00:132");

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, new HashMap<>());
        Assert.assertEquals(YoutubeDLCommander.getBestAudio(),command);
    }

    @Test
    public void shouldReturnTimedVideo(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("12");
        parametersDTO.setTimeTo("13");

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, new HashMap<>());
        Assert.assertEquals(YoutubeDLCommander.getBestVideo(),command);
    }

    @Test
    public void shouldReturnVideo(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);


        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, new HashMap<>());
        Assert.assertEquals(YoutubeDLCommander.getBestVideo(),command);
    }

    @Test
    public void shouldReturnVideo2(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("00:00:12");
        parametersDTO.setTimeTo("00:00:123");

        YoutubeDLCommander command = factoryInterface.getCommand(parametersDTO, new HashMap<>());
        Assert.assertEquals(YoutubeDLCommander.getBestVideo(),command);
    }
}
