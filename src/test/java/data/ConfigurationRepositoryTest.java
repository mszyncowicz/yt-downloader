package data;

import annotation.InjectEntityMangaer;
import model.Configuration;
import model.SessionFactoryExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SessionFactoryExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigurationRepositoryTest {

    @InjectEntityMangaer
    private ConfigurationRepositoryImpl configurationRepository;

    private String bla = "bla";

    private Configuration configuration;

    @BeforeAll
    public void beforeAll()
    {
        configurationRepository = new ConfigurationRepositoryImpl();
    }

    @BeforeEach
    public void init(){

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
        Assertions.assertNotNull(configurationRepository.getAny());
    }

    @Test
    public void shouldBeSame(){
        Assertions.assertEquals(configuration.getId(),(configurationRepository.getAny().getId()));
    }

    @Test
    public void shouldBeUpdated(){
        Assertions.assertEquals(bla, configurationRepository.getAny().getVideoFolder());
    }

    @Test
    @Disabled
    public void lol()
    {
        BigDecimal cena1 = BigDecimal.valueOf(140.85);
        BigDecimal cena = BigDecimal.valueOf(140.85);
        int iloscSztuk = 25;
        BigDecimal prowizja = BigDecimal.valueOf(0.00375);
        BigDecimal belkatax = BigDecimal.valueOf(0.19);

        BigDecimal cenaAll = cena.multiply(BigDecimal.valueOf(iloscSztuk));
        BigDecimal doZaplaty = cenaAll.add(cenaAll.multiply(prowizja));

        BigDecimal wantedIncome = doZaplaty.add(doZaplaty.multiply(BigDecimal.valueOf(0.01)));

        BigDecimal currentPrice = BigDecimal.ZERO;
        System.out.println(doZaplaty);

        System.out.println(wantedIncome);
        int i = 0;
        while (currentPrice.compareTo(wantedIncome) < 0)
        {
            i++;
            cena = cena.add(BigDecimal.valueOf(0.01));
            if (i % 10 == 0) {
                System.out.println("trying price " + cena);
            }
            BigDecimal cenaAll2 = cena.multiply(BigDecimal.valueOf(iloscSztuk));
            BigDecimal doZaplaty2 = cenaAll2.subtract(cenaAll2.multiply(prowizja));
            currentPrice = doZaplaty2.subtract(cenaAll2.subtract(cenaAll).multiply(belkatax));
            if (i % 10 == 0) {
                System.out.println("income percent = " + currentPrice.multiply(BigDecimal.valueOf(100)).divide(doZaplaty, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(100)));


                System.out.println("price rise percent = " + cena.multiply(BigDecimal.valueOf(100)).divide(cena1, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(100)));

            }
        }
        System.out.println(cena);

    }

    @Test
    @Disabled

    public void iledomil()
    {
        BigDecimal cena1 = BigDecimal.valueOf(140.85);
        BigDecimal cena = BigDecimal.valueOf(140.85);
        int iloscSztuk = 25;
        BigDecimal prowizja = BigDecimal.valueOf(0.00375);
        BigDecimal belkatax = BigDecimal.valueOf(0.19);

        BigDecimal cenaAll = cena.multiply(BigDecimal.valueOf(iloscSztuk));
        BigDecimal doZaplaty = cenaAll.add(cenaAll.multiply(prowizja));

       // BigDecimal doZaplaty = BigDecimal.valueOf(10000);
        int i = 0;
        while (doZaplaty.compareTo(BigDecimal.valueOf(400000)) < 0)
        {
            i++;
            doZaplaty = doZaplaty.add(doZaplaty.multiply(BigDecimal.valueOf(0.10)));
        }

        System.out.println(i);
    }
}
