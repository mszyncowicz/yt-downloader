import api.Commander;
import api.Locator;
import downloader.MediaToolExecutor;
import downloader.WrongParametersException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;


@Slf4j
@Startup
@Singleton
public class Main {

    @Inject
    Logger logger;

    static void lookForApp(String app){
        /*
        try {
            Process p = Runtime.getRuntime().exec(app);
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = bre.readLine()) != null) {
                if (line.contains("not recognized")) {
                    throw new Error("You need " + app + " installed in environment variables to run this application");
                }
            }
        }catch (IOException ex){
            throw new Error("You need " + app + " installed in environment variables to run this application");

        }*/

        MediaToolExecutor mediaToolExecutor = MediaToolExecutor.basicExecutor();
        try {
            mediaToolExecutor.execute(    c-> app, Collections.emptyMap());
        } catch (Exception e){
            throw new Error("You need " + app + " installed in environment variables to run this application");
        } finally {
            if (mediaToolExecutor.isError()){
        log.error("P" + MediaToolExecutor.processId + ": " +mediaToolExecutor.getErrorMessage());
    }
}
    }

//@PostConstruct
public void lookup() throws ClassNotFoundException, IOException, SQLException {
        update();
        lookForApp("ffmpeg");
        Class.forName("org.h2.Driver");
        Properties prop = new Properties();
        InputStream in = Main.class.getResourceAsStream("h2.properties");
        prop.load(in);
        in.close();
        Connection connection = DriverManager.getConnection(prop.getProperty("url"),prop.getProperty("username"),prop.getProperty("password"));
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
        log.info("Successfully initialized file database");
    }

    @PostConstruct
    public void turnOffHibernateLog() {
        java.util.logging.Logger.getLogger("stdout").setLevel(Level.SEVERE);
    }

    @Schedule(hour = "*/1",persistent = false)
    public void update(){
        lookForApp("yt-dlp -U");
    }
}
