package controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import downloader.LogObserver;
import dto.LogObserverDTO;
import dto.LogObserversDTO;
import model.Record;
import model.State;

@Path("/default/say")
public class StockController {

    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Record getString(){
        return new Record("sf","sf");
    }

    @GET
    @Path("/bye")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBye(){
        return "ijioda";
    }

    @GET
    @Path("/log")
    @Produces(MediaType.APPLICATION_JSON)
    public LogObserversDTO getLog(){

        List<LogObserverDTO> dtos = new ArrayList<>(3);

        LogObserverDTO logObserverDTO = new LogObserverDTO();
        logObserverDTO.setState(State.downloading.name());
        logObserverDTO.setPercentage(75F);
        logObserverDTO.setRecordUUID("a");
        logObserverDTO.setLastMessage("LastMessage");
        logObserverDTO.setTitle("Zaadw");

        dtos.add(logObserverDTO);

        logObserverDTO = new LogObserverDTO();
        logObserverDTO.setState(State.converting.name());
        logObserverDTO.setPercentage(65F);
        logObserverDTO.setRecordUUID("sddsssssd");
        logObserverDTO.setLastMessage("LastMessage");
        logObserverDTO.setTitle("segfsgdsgrbdrg");

        dtos.add(logObserverDTO);

        LogObserversDTO logObserversDTO = new LogObserversDTO();
        logObserversDTO.setLogObserversDTOList(dtos);
        return logObserversDTO;
    }

}
