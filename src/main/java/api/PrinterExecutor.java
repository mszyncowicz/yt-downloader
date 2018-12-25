package api;

import downloader.WrongParametersException;

import java.util.Map;

public interface PrinterExecutor extends Executor {
    @Override
    default Object execute(Commander commander, Map<String, String> args) throws WrongParametersException {

        if (commander instanceof LinkPrinter)return execute((LinkPrinter) commander,args);
        else throw new WrongParametersException();
    }

    public Object execute(LinkPrinter linkPrinter, Map<String, String> args) throws WrongParametersException;
}
