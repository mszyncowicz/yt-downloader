package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import model.Session;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude="session")
@ToString(exclude = "session")
@Table(name = "record")
public class Record {

    @Id
    private UUID id;

    private State state;

    @Column(name = "link")
    private String link;

    @Column(name = "command")
    private String command;

    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name="session_id", nullable=false)
    @JsonIgnore
    private Session session;

    @CreationTimestamp
    private Date date;

    public Record(){
        this.id = UUID.randomUUID();
    }

    public Record(String link, String command){
        state = State.downloading;
        this.link = link;
        this.command = command;
    }

}
