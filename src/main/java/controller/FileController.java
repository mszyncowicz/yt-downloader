package controller;

import dto.ConfigurationDTO;
import dto.GeneralMessageDTO;
import lombok.Setter;
import model.Configuration;
import service.ConfigurationService;
import service.SessionService;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.transaction.*;
import javax.transaction.NotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/file")
public class FileController {

    @Any
    @Setter
    @Inject
    SessionService sessionService;

    @Any
    @Setter
    @Inject
    ConfigurationService configurationService;

    @Setter
    @Inject
    UserTransaction userTransaction;

    @GET
    @Path("/configuration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        ConfigurationDTO configurationDTO = getFromService();
        userTransaction.commit();
        return Response.ok(configurationDTO).build();
    }

    @POST
    @Path("/configure")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateConfiguration(ConfigurationDTO configurationDTO) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        if (configurationDTO.check()){
            update(configurationDTO);
        }else {
            userTransaction.commit();
            GeneralMessageDTO generalMessageDTO = new GeneralMessageDTO();
            generalMessageDTO.setMessage("Invalid directory");
            generalMessageDTO.setCode(500);
            return Response.ok(generalMessageDTO).status(500).build();
        }
        userTransaction.commit();
        return Response.ok().build();
    }

    ConfigurationDTO getFromService(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setVideoFolder(configurationService.getVideoFolder().getAbsolutePath());
        configurationDTO.setAudioFolder(configurationService.getAudioFolder().getAbsolutePath());
        return configurationDTO;
    }

    boolean update(ConfigurationDTO configurationDTO){
        return configurationService.updateConfiguration(configurationDTO);
    }
}
