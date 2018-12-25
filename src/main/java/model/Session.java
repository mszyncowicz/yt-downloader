package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import lombok.ToString;
import model.Record;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Session {
    public static final int scale = 20;

    @Id
    private UUID id;

    @Column(length = scale)
    private String token;

    @OneToMany(mappedBy = "session")
    private List<Record> records;

    public Session() {
        id = UUID.randomUUID();
        token = generateRandom(scale);
        records = new ArrayList<>();
    }

    public static String generateRandom(int scale) {
        String space = "ABCDEFGHIJKLMNOPQRSTUWXVYZabcdefghijklmnopqrstuwvxyz12345667890";
        Random random = new Random();
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < scale; i++) {
            int getInt = random.nextInt(space.length());
            result.append(space.charAt(getInt));

        }
        return result.toString();
    }

}
