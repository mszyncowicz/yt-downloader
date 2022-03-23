package model;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
public class SessionTest {


    @Test
    public void randomness(){
         String one = Session.generateRandom(Session.scale);
        String two = Session.generateRandom(Session.scale);

        Assertions.assertTrue(one.length() == Session.scale);
        Assertions.assertTrue(two.length() == Session.scale);
        Assertions.assertFalse(one.equals(two));

        int as = 0;
        for(int i=0; i<one.length(); i++){
            if (one.charAt(i) == two.charAt(i)) as++;
        }

        Assertions.assertTrue(as<40);

        log.info(one);
        log.info(two);
    }

    @Test
    public void equalsTest(){
        Session one = new Session();
        one.setToken(Session.generateRandom(20));
        one.setId(UUID.randomUUID());
        one.setRecords(Collections.EMPTY_LIST);

        Session two = new Session();
        two.setToken(one.getToken());
        two.setId(one.getId());
        two.setRecords(one.getRecords());

        Assertions.assertTrue(one.equals(two));
    }

}