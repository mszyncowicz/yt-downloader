package downloader;

import api.Observable;
import api.Observer;
import model.Record;
import model.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class LogObserverTest {

    LogObserver observer;

    ObserverTest observerTest;
    @Before
    public void init() throws FileNotFoundException {
        observerTest = new ObserverTest(new FileInputStream("ObserverTest.txt"));
        observer = new LogObserver(new Record());
        observerTest.addObserver(observer);
    }

    @Test
    public void shouldPercentageChange(){
        observerTest.startWhole();
        Assert.assertEquals(100f,observer.getPercentage().floatValue(), 0.0001);
    }

    @Test
    public void shouldChangeTitle(){
        observerTest.startWhole();
        Assert.assertNotNull(observer.getTitle());
    }

    @Test
    public void titleShouldNotContainWebmExt(){
        observerTest.startWhole();
        System.out.println(observer.getTitle());
        Assert.assertFalse(observer.getTitle().toLowerCase().contains("\\.webm"));
    }
    @Test
    public void titleShouldNotContainYtCode(){
        observerTest.startWhole();
        Assert.assertFalse(observer.getTitle().contains("-ecc_G1W8VvU"));
    }
    @Test
    public void shouldChangeRecordState(){
        observerTest.startWhole();
        Assert.assertEquals(observer.getRecord().getState(),State.converting);
    }

    static class ObserverTest implements Observable {

        List<Observer> observers;

        InputStream inputStream;

        ObserverTest(InputStream inputStream){
            observers = new LinkedList<>();
            this.inputStream = inputStream;
        }

        boolean started = false;
        BufferedReader br;
        void startWhole(){
            try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                for (Observer observer : observers){
                    observer.update(this,line);
                }
            }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        boolean lineByLine(){
            try {
                if (!started) {
                    br = new BufferedReader(new InputStreamReader(inputStream));
                }
                return br.readLine() != null;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public List<Observer> getObservers() {
            return observers;
        }
    }
}
