package downloader.converter;

import api.Commander;
import downloader.WrongParametersException;
import lombok.Getter;
import oracle.jrockit.jfr.StringConstantPool;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;

public abstract class FFMPEGCommander implements Commander {
    @Getter
    protected String[] required;

    protected void setRequired(String[] required){
        this.required = required;
    }

    protected FFMPEGCommander(String[] required){
        this.required = required;
    }

    public static FFMPEGCommander convertFromatToFormat(String file, String format){
        return new ConvertFormatToFormat(file,format);
    }

    public static FFMPEGCommander mergeVideoAndAudio(String fileOne, String fileTwo, String container){
        return new MergeVideoAndAudio(fileOne,fileTwo,container);
    }

    public static FFMPEGCommander cutMedia(String file, LocalTime begin, LocalTime end) throws WrongParametersException {
        if (end.isBefore(begin) || begin.equals(end)) throw new WrongParametersException();
        return new CutMedia(file,begin,end);
    }

    enum Args{
        output,
        audio_format,
        vidueo_format,
        uuid;
    }

}

class ConvertFormatToFormat extends FFMPEGCommander{

    private String file,format;
    public ConvertFormatToFormat(String file, String format) {
        super(new String[]{Args.uuid.name(),Args.output.name()});
        this.file = file;
        this.format = format;
    }

    @Override
    public String getCommand(Map<String, String> args) throws WrongParametersException {
        argsCheck(args,required);
        return null;
    }
}

class MergeVideoAndAudio extends FFMPEGCommander{

    String fileOne,fileTwo,container;

    public MergeVideoAndAudio(String fileOne, String fileTwo, String container) {
        super(new String[]{Args.uuid.name(),Args.output.name()});
        this.fileOne = fileOne;
        this.fileTwo = fileTwo;
        this.container = container;
    }

    @Override
    public String getCommand(Map<String, String> args) throws WrongParametersException {
        argsCheck(args,required);
        return null;
    }
}

class CutMedia extends FFMPEGCommander{

    String file;
    LocalTime begin,end;

    public CutMedia(String file, LocalTime begin, LocalTime end) {
        super(new String[]{Args.uuid.name(),Args.output.name()});
        this.file = file;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String getCommand(Map<String, String> args) throws WrongParametersException {
        argsCheck(args,required);
        return null;
    }

}
