package dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Configuration;
import org.opensaml.xmlsec.signature.P;

import java.io.File;

@Getter
@Setter
@ToString
public class ConfigurationDTO {
    private String videoFolder;
    private String audioFolder;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        else{
            if (o instanceof ConfigurationDTO){
                ConfigurationDTO configuration = (ConfigurationDTO) o;
                if (
                        new File(audioFolder).getAbsolutePath().equals(new File(((ConfigurationDTO) o).audioFolder).getAbsolutePath()) &&
                                new File(videoFolder).getAbsolutePath().equals(new File(((ConfigurationDTO) o).videoFolder).getAbsolutePath())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean check(){
        boolean result = true;

        if (videoFolder !=null){
           result = checkFolder(videoFolder);
        }

        if (audioFolder != null){
            return checkFolder(audioFolder);
        }

        return result;
    }

    private boolean checkFolder(String folder){
        File file = new File(folder);
        String absolutePath = file.getAbsolutePath();
        if (!absolutePath.contains(File.separator)) {
            return false;
        }
        return true;
    }
}
