package bean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileMoverBeanTest {
    final String FILE_NAME = "fileName.txt";

    private File one,two;

    @Before
    public void before() throws IOException {
        one = new File(new File(new File("").getAbsolutePath()),FILE_NAME);
        two = new File(new File(new File("").getAbsolutePath()),"fileName" + " (" + 1 + ").txt");
        one.createNewFile();
        two.createNewFile();
    }

    @Test
    public void shouldGetNewName(){
        File dir = one.getParentFile();
        assertTrue(dir.exists());
        String newFileName = FileMoverBean.getNewFileName(dir, FILE_NAME);
        assertEquals("fileName"+ " (" + 2 +").txt",newFileName);
    }

    @Test
    public void shouldFindExtension(){
        Matcher matcher = FileMoverBean.EXTENSION_PATTERN.matcher(FILE_NAME);
        assertTrue(matcher.find());
    }

    @After
    public void removeAfter(){
        one.delete();
        two.delete();
    }
}
