package printer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.YtMediaDetails;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class YtJsonReader {

    ObjectMapper mapper;

    public YtJsonReader(){
        mapper = new ObjectMapper();
    }

    public YtMediaDetails getDataFrom(String json){
        try {
            YtMediaDetails ytMediaDetails = new YtMediaDetails();
            JsonNode jsonNode = mapper.readTree(json);
            ytMediaDetails.setLength(jsonNode.findValue("duration").asText());
            ytMediaDetails.setThumbnail(jsonNode.findValue("thumbnail").asText());
            ytMediaDetails.setTitle(jsonNode.findValue("title").asText());
            ytMediaDetails.setAuthor(jsonNode.findValue("uploader_id").asText());
            List<String> formats = jsonNode.findValues("format").stream().map(p -> p.asText()).collect(Collectors.toList());
            ytMediaDetails.setFormats(formats);
            return ytMediaDetails;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
