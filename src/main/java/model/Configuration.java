package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.util.UUID;

@ToString
@Data
@Entity
public class Configuration {

    @Id
    private UUID id;
    private String audioFolder;
    private String videoFolder;


}
