package service;

import data.ConfigurationRepository;
import dto.ConfigurationDTO;
import lombok.Getter;
import lombok.Setter;
import model.Configuration;
import java.util.UUID;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.io.File;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService{

    public static final String DEFAULT_PATH = System.getProperty("user.home") + File.separator + "Downloads";

    @Setter
    @Getter
    @Any
    @Inject
    private ConfigurationRepository configurationRepository;

    private File audioFolder = null;
    private File videoFolder = null;

    @Override
    public boolean updateConfiguration(ConfigurationDTO configurationDTO) {
        Configuration config = configurationRepository.getAny();
        if (config == null){
            config = createNewConfig();
        }

        if (configurationDTO.getAudioFolder() != null){
            audioFolder = new File(configurationDTO.getAudioFolder());
            config.setAudioFolder(configurationDTO.getAudioFolder());
        }

        if (configurationDTO.getVideoFolder() != null){
            videoFolder = new File(configurationDTO.getVideoFolder());
            config.setVideoFolder(configurationDTO.getVideoFolder());
        }

        return configurationRepository.save(config) != null;
    }

    @Override
    public File getAudioFolder() {
        if (audioFolder != null){
            return audioFolder;
        }
        Configuration config = configurationRepository.getAny();

        if (config == null) return new File(DEFAULT_PATH);
        else {
            return new File(config.getAudioFolder());
        }
    }

    @Override
    public File getVideoFolder() {
        if (videoFolder != null){
            return videoFolder;
        }
        Configuration config = configurationRepository.getAny();

        if (config == null) return new File(DEFAULT_PATH);
        else {
            return new File(config.getVideoFolder());
        }
    }

    Configuration createNewConfig(){
        Configuration configuration = new Configuration();
        configuration.setId(UUID.randomUUID());
        configuration.setVideoFolder(DEFAULT_PATH);
        configuration.setAudioFolder(DEFAULT_PATH);
        return configuration;
    }

}
