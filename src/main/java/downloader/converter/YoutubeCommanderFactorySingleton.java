package downloader.converter;

import downloader.YoutubeCommanderFactory;
import downloader.YoutubeDLCommander;
import downloader.YoutubeDLCommanderFactoryInterface;
import dto.YTParametersDTO;

import javax.ejb.Singleton;
import java.util.Map;

@Singleton
public class YoutubeCommanderFactorySingleton implements YoutubeDLCommanderFactoryInterface {

    @Override
    public YoutubeDLCommander getCommand(YTParametersDTO parametersDTO, Map<String, String> parameters) {
        return YoutubeCommanderFactory.INSTANCE.getCommand(parametersDTO,parameters);
    }
}
