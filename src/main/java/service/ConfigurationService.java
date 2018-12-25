package service;

import dto.ConfigurationDTO;

import java.io.File;

public interface ConfigurationService {

    boolean updateConfiguration(ConfigurationDTO confiurationDTO);

    File getAudioFolder();

    File getVideoFolder();

}
