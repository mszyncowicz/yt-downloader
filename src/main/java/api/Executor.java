package api;

import downloader.WrongParametersException;

import java.util.Map;

@FunctionalInterface
public interface Executor {
    Object execute(Commander commander, Map<String, String> args) throws WrongParametersException;
}
