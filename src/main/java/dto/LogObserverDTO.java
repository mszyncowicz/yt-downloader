package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogObserverDTO {

    String recordUUID;

    String state;

    Float percentage;

    String lastMessage;

    String title;
}
