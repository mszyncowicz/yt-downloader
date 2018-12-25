package data;

import model.Configuration;
import model.SessionFactoryRule;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ConfigurationRepositoryTest {
    @Rule
    public SessionFactoryRule sessionFactoryRule = new SessionFactoryRule();

    ConfigurationRepositoryImpl configurationRepository;

    String bla = "bla";

    Configuration configuration;

    @Before
    public void init(){
        configurationRepository = new ConfigurationRepositoryImpl();
        sessionFactoryRule.injectManager(configurationRepository);

        configurationRepository.getEntityManager().getTransaction().begin();
        configuration = generateConfiguration();
        configurationRepository.save(configuration);
        configurationRepository.getEntityManager().getTransaction().commit();

        configurationRepository.getEntityManager().getTransaction().begin();
        configuration.setVideoFolder(bla);
        configurationRepository.save(configuration);
        configurationRepository.getEntityManager().getTransaction().commit();

    }

    private Configuration generateConfiguration(){
        Configuration configuration = new Configuration();
        configuration.setId(UUID.randomUUID());
        configuration.setAudioFolder("audio folder");
        configuration.setVideoFolder("video fssf");
        return configuration;
    }

    @Test
    public void shouldBeSaved(){
        Assert.assertNotNull(configurationRepository.getAny());
    }

    @Test
    public void shouldBeSame(){
        Assert.assertTrue(configuration.getId().equals(configurationRepository.getAny().getId()));
    }

    @Test
    public void shouldBeUpdated(){
        Assert.assertEquals(bla, configurationRepository.getAny().getVideoFolder());
    }
}
