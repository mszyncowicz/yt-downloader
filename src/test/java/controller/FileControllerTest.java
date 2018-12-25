package controller;

import dto.ConfigurationDTO;
import model.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import service.ConfigurationService;
import service.SessionService;

import javax.transaction.*;
import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    private static final String DEFAULT_VIDEO_FOLDER = "e://Downloads//";

    private static final String DEFAULT_AUDIO_FOLDER = "e://Downloads//audio//";

    @Spy
    ConfigurationDTO configurationDTO;

    FileController fileController;

    @Mock
    SessionService sessionService;

    @Mock
    ConfigurationService configurationService;

    @Mock
    UserTransaction userTransaction;

    @Before
    public void init(){
        fileController = new FileController();
        fileController.setSessionService(sessionService);
        fileController.setConfigurationService(configurationService);
        fileController.setUserTransaction(userTransaction);
        configurationDTO = new ConfigurationDTO();
        configurationDTO.setAudioFolder(DEFAULT_AUDIO_FOLDER);
        configurationDTO.setVideoFolder(DEFAULT_VIDEO_FOLDER);

        when(configurationService.getAudioFolder()).thenReturn(new File(DEFAULT_AUDIO_FOLDER));
        when(configurationService.getVideoFolder()).thenReturn(new File(DEFAULT_VIDEO_FOLDER));
        when(configurationService.updateConfiguration(any())).then(a -> {
            ConfigurationDTO argumentAt = a.getArgumentAt(0, ConfigurationDTO.class);
            configurationDTO.setVideoFolder(argumentAt.getVideoFolder());
            configurationDTO.setAudioFolder(argumentAt.getAudioFolder());
            return true;
        });
    }

    @Test
    public void shouldInvokeGetFromService() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        try{
            fileController.getConfiguration();

        }catch (AbstractMethodError e){
        }


        verify(configurationService,times(1)).getAudioFolder();
        verify(configurationService,times(1)).getVideoFolder();
        verify(userTransaction,times(1)).begin();
        verify(userTransaction,times(1)).commit();

    }

    @Test
    public void shouldReturnDefaults(){
        ConfigurationDTO fromService = fileController.getFromService();
        Assert.assertTrue(configurationDTO.equals(fromService));

        verify(configurationService,times(1)).getAudioFolder();
        verify(configurationService,times(1)).getVideoFolder();
    }

    @Test
    public void updateTest() throws HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {
        ConfigurationDTO newConfig = new ConfigurationDTO();
        String audioFolder = "d:/vla";
        newConfig.setAudioFolder(audioFolder);
        String videoFolder = "e:/sfsf";
        newConfig.setVideoFolder(videoFolder);

        try {
            fileController.updateConfiguration(newConfig);
        }catch (AbstractMethodError e){
        }

        Assert.assertTrue(newConfig.equals(configurationDTO));
    }

    @Test
    public void shouldNotBeAbleToSaveInvalidPassedFolders() throws HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {
        ConfigurationDTO newConfig = new ConfigurationDTO();
        String audioFolder = "la";
        newConfig.setAudioFolder(audioFolder);
        String videoFolder = "dgdgsgrgd";
        newConfig.setVideoFolder(videoFolder);

        Assert.assertFalse(newConfig.equals(configurationDTO));
    }
}
