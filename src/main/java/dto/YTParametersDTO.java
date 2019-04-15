package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YTParametersDTO {
    @JsonProperty( required = true)
    private String mediaType;

    @JsonProperty( required = true)
    private String url;
    private String timeFrom;
    private String timeTo;

    @JsonProperty(value="isTimed", required = true)
    private boolean isTimed;

    @JsonProperty(value="overwrite")
    private boolean overwrite;
}
