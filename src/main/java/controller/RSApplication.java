package controller;

import controller.filter.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RSApplication extends Application {
    private Set<Object> singletons = new HashSet<>();

    public RSApplication(){
        singletons.add(new FileController());

        singletons.add(new SessionController());
        singletons.add(new StockController());
        singletons.add(new DownloadController());
    }

    public Set<Class<?>> getClasses()
    {

        return getRestClasses();
    }

    public Set<Class<?>> getRestClasses()
    {
        Set<Class<?>> s = new HashSet<Class<?>>();
        return s;
    }


    public Set<Object> getSingletons()
    {
        return this.singletons;
    }
}
