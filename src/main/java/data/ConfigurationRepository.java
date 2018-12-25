package data;

import model.Configuration;

public interface ConfigurationRepository extends Repository<Configuration>{
    Configuration getAny();
}
