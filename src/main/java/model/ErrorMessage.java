package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ErrorMessage {
    private String errorMessage;
    private String link;
    private String sessionToken;

    public ErrorMessage(String errorMessage, String link, String sessionToken) {
        this.errorMessage = errorMessage;
        this.link = link;
        this.sessionToken = sessionToken;
    }
}
