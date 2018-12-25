package printer;

import api.Executor;
import api.LinkPrinter;
import api.PrinterExecutor;
import downloader.MediaToolExecutor;
import downloader.WrongParametersException;

import java.util.Map;

public class LinkPrinterExecutor implements PrinterExecutor {
    private MediaToolExecutor mediaToolExecutor;

    private LinkPrinterExecutor(){
        mediaToolExecutor = MediaToolExecutor.basicExecutor();
    }

    public static LinkPrinterExecutor basicPrinterExecutor(){
        LinkPrinterExecutor executor = new LinkPrinterExecutor();
        executor.mediaToolExecutor.setFileLocator(DefaultPrinterLocator.defaultPrinterLocator);
        return executor;
    }

    @Override
    public Object execute(LinkPrinter linkPrinter, Map<String, String> args) throws WrongParametersException {
        return mediaToolExecutor.execute(linkPrinter,args);
    }
}
