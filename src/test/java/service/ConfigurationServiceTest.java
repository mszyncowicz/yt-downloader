package service;

import data.ConfigurationRepository;
import dto.ConfigurationDTO;
import model.Configuration;

import java.io.File;
import java.util.UUID;
import org.jboss.weld.exceptions.UnsupportedOperationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @Mock
    ConfigurationRepository configurationRepository;

    ConfigurationServiceImpl configurationService;

    List<Configuration> configurationList;

    @Before
    public void init(){
        configurationService = spy(ConfigurationServiceImpl.class);
        configurationService.setConfigurationRepository(configurationRepository);

        configurationList = new ArrayList<>();

        when(configurationRepository.save(anyObject())).then(a -> {
            Configuration argumentConfig = a.getArgumentAt(0,Configuration.class);
            if (!configurationList.isEmpty()){
                if (argumentConfig.getId().equals(configurationList.get(0).getId())){
                    configurationList.remove(configurationList.get(0));
                    configurationList.add(argumentConfig);
                    return argumentConfig;
                }
                return null;
            }
            else
            {
                configurationList.add(argumentConfig);
                return argumentConfig;
            }
        });

        when(configurationRepository.getAny()).then(a->{
            if (configurationList.isEmpty()) {
                return null;
            }
            else
            {
                return configurationList.get(0);
            }
        });

        when(configurationRepository.getById(any())).thenThrow(new UnsupportedOperationException());
    }

    public void prepareConfig(){
        Configuration configuration = new Configuration();
        configuration.setId(UUID.randomUUID());
        configuration.setAudioFolder("path/musicFolder");
        configuration.setVideoFolder("path/videoFolder");

        configurationList.add(configuration);
    }

    @Test
    public void defaultAudioFolderTest(){
        File audioFolder = configurationService.getAudioFolder();
        Assert.assertNotNull(audioFolder);
        Assert.assertEquals(ConfigurationServiceImpl.DEFAULT_PATH,audioFolder.getAbsolutePath());
    }

    @Test
    public void defaultVideoFolderTest(){
        File videoFolder = configurationService.getVideoFolder();
        Assert.assertNotNull(videoFolder);
        Assert.assertEquals(ConfigurationServiceImpl.DEFAULT_PATH,videoFolder.getAbsolutePath());
    }

    @Test
    public void updateVideoFolderOnly(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        String newFolder = "newFolder";
        configurationDTO.setVideoFolder(newFolder);

        configurationService.updateConfiguration(configurationDTO);

        Assert.assertFalse(configurationList.isEmpty());
        Configuration result = configurationList.get(0);

        Assert.assertEquals(newFolder,result.getVideoFolder());
        Assert.assertEquals(ConfigurationServiceImpl.DEFAULT_PATH,result.getAudioFolder());

    }

    @Test
    public void updateAudioFolderOnly(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        String newFolder = "newFolder";
        configurationDTO.setAudioFolder(newFolder);

        configurationService.updateConfiguration(configurationDTO);

        Assert.assertFalse(configurationList.isEmpty());
        Configuration result = configurationList.get(0);

        Assert.assertEquals(newFolder,result.getAudioFolder());
        Assert.assertEquals(ConfigurationServiceImpl.DEFAULT_PATH,result.getVideoFolder());

    }

    @Test
    public void updateNotExistTest(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        String videoFolder = "newFolder";
        String audioFolder = "audiosdsFolder";

        configurationDTO.setAudioFolder(audioFolder);
        configurationDTO.setVideoFolder(videoFolder);
        configurationService.updateConfiguration(configurationDTO);

        Assert.assertEquals(1,configurationList.size());


        Configuration result = configurationList.get(0);

        Assert.assertEquals(audioFolder,result.getAudioFolder());
        Assert.assertEquals(videoFolder,result.getVideoFolder());
        verify(configurationService).createNewConfig();
    }

    @Test
    public void updateTest(){
        prepareConfig();

        Assert.assertEquals(1,configurationList.size());

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        String videoFolder = "newFolder";
        String audioFolder = "audiosdsFolder";

        configurationDTO.setAudioFolder(audioFolder);
        configurationDTO.setVideoFolder(videoFolder);
        configurationService.updateConfiguration(configurationDTO);

        Assert.assertEquals(1,configurationList.size());

        Configuration result = configurationList.get(0);

        Assert.assertEquals(audioFolder,result.getAudioFolder());
        Assert.assertEquals(videoFolder,result.getVideoFolder());

        verify(configurationService,times(0)).createNewConfig();

    }


}
