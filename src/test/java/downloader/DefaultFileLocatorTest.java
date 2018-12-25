package downloader;

import lombok.extern.slf4j.Slf4j;
import model.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
public class DefaultFileLocatorTest {

    List<File> dirsToRemove = new LinkedList<>();

    public static BufferedReader getReaderFromString(String string){
        Reader reader = new StringReader(string);
        return new BufferedReader(reader);
    }

    public static Optional<File> findFromString(String string,String directory){
        DefaultFileLocator defaultFileLocator = DefaultFileLocator.defaultFileLocator;

        Optional<File> locate = defaultFileLocator.locate(getReaderFromString(string), directory);

        return locate;

    }

    @Test
    public void shouldReturnNull(){
        String string = "NotFound";
        Optional<File> locate = findFromString(string,System.getProperty("user.dir"));
        Assert.assertFalse(locate.isPresent());
    }
    @Test
    public void shouldFindPomXml(){
        String string = "pom.xml";
        Optional<File> locate = findFromString("file" + string,System.getProperty("user.dir"));
        Assert.assertTrue(locate.isPresent());
    }
    @Test
    public void shouldFindInDirectory(){
        String directory = "testDir";
        File dir = new File(directory);
        dir.mkdirs();
        dirsToRemove.add(dir);

        String fileName = "testFile.txt";
        File file = new File(directory + "/" + fileName);
        try{
            file.createNewFile();
        }catch (IOException ex){
            Assert.fail();
        }

        Optional<File> fromString = findFromString("file://" + fileName, directory);

        Assert.assertTrue(fromString.isPresent());
    }

    @Test
    public void shouldFindInDirectory_longFileName() throws IOException {
        String directory = "testDirLongName";
        File dir = new File(directory);
        dir.mkdirs();
        dirsToRemove.add(dir);

        String fileName = Session.generateRandom(251) + ".txt"; //looks like windows can only add files within a Byte space
        File file = new File(directory + "/" + fileName);
        try{
            file.createNewFile();
        }catch (IOException ex){
            ex.printStackTrace();
            Assert.fail();
        }

        Optional<File> fromString = findFromString("file://" +fileName, directory);

        Assert.assertTrue(fromString.isPresent());
    }

    @After
    public void removeDirs(){
        for (File file : dirsToRemove){
            for (File child : file.listFiles()){
                child.delete();
            }
            file.delete();
            Assert.assertFalse(file.exists());
        }
    }
}