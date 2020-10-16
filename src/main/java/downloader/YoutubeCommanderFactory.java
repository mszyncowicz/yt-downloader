package downloader;

import dto.YTParametersDTO;
import model.MediaType;
import org.apache.commons.lang.time.DurationFormatUtils;

import javax.ejb.Singleton;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public enum YoutubeCommanderFactory implements YoutubeDLCommanderFactoryInterface {
    INSTANCE;

    public static final String TIME_MATCHER = "[0-9]+";

    public YoutubeDLCommander getCommand(YTParametersDTO parametersDTO, Map<String, String> parameters) {
        MediaType mediaType = MediaType.valueOf(parametersDTO.getMediaType());

        if (parametersDTO.isTimed()) {
            Optional<String> postArgs = getPostArgs(parametersDTO.getTimeFrom(), parametersDTO.getTimeTo());
            if (postArgs.isPresent()) {
                parameters.put(YoutubeDLCommander.TIME_PARAM, postArgs.get());
            }
        }
        return mediaType.getCommander();
    }

    Optional<String> getPostArgs(String timeFrom, String timeTo) {
        if (timeFrom == null || timeTo == null) return Optional.ofNullable(null);

        else if (!timeFrom.matches(TIME_MATCHER) || !timeTo.matches(TIME_MATCHER)) return Optional.ofNullable(null);
        else {
            final Integer timeFromInt = Integer.valueOf(timeFrom);
            final Integer timeToInt = Integer.valueOf(timeTo);
            Duration durationFrom0ToStart = Duration.ofSeconds(timeFromInt);
            Duration durationFrom0ToEnd = Duration.ofSeconds(timeToInt - timeFromInt);
            String lvResult = "-ss " + DurationFormatUtils.formatDuration(durationFrom0ToStart.toMillis(), "HH:mm:ss", true) + " -t " + DurationFormatUtils.formatDuration(durationFrom0ToEnd.toMillis(), "HH:mm:ss", true);
            return Optional.of(lvResult);
        }
    }
}
