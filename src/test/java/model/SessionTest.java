package model;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class SessionTest {


    @Test
    public void randomness(){
         String one = Session.generateRandom(Session.scale);
        String two = Session.generateRandom(Session.scale);

        Assert.assertTrue(one.length() == Session.scale);
        Assert.assertTrue(two.length() == Session.scale);
        Assert.assertFalse(one.equals(two));

        int as = 0;
        for(int i=0; i<one.length(); i++){
            if (one.charAt(i) == two.charAt(i)) as++;
        }

        Assert.assertTrue(as<40);

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

        Assert.assertTrue(one.equals(two));
    }

}