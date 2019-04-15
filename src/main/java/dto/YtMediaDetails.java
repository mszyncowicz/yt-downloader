package dto;

import lombok.Data;

import java.util.List;

@Data
public class YtMediaDetails {
    private String title;
    private String author;
    private String length;
    private String thumbnail;
    private List<String> formats;
}
