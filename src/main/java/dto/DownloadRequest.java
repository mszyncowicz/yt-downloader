package dto;

import lombok.Data;
import model.State;

@Data
public class DownloadRequest {

    String mediaType;

    String link;

}
