package api;

import downloader.WrongParametersException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
public interface Commander {
    String getCommand(Map<String,String> args) throws WrongParametersException;

    default void argsCheck(Map<String,String> args, String[] requiredKeys) {
        List<String> strings = Arrays.asList(requiredKeys);

        List<String> stringStream = strings.stream().filter(a -> args.keySet().contains(a)).collect(Collectors.toList());
        if (stringStream.size() != strings.size()){
            List<String> missingArgs = strings.stream().filter(a-> !stringStream.contains(a)).collect(Collectors.toList());
             throw new IllegalArgumentException("Missing args: " + missingArgs.toString());
        }
    }
}
