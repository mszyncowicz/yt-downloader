package downloader;

import dto.YTParametersDTO;

import java.util.Map;

public interface YoutubeDLCommanderFactoryInterface {
    YoutubeDLCommander getCommand(YTParametersDTO parametersDTO, Map<String,String> parameters);
}
