package bean;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@MockitoSettings(strictness = Strictness.LENIENT)
public class FileMoverBeanTest {
    final String FILE_NAME = "fileName.txt";

    private File one,two;

    @BeforeEach
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

    @AfterEach
    public void removeAfter(){
        one.delete();
        two.delete();
    }
}
