package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.omg.PortableInterceptor.Interceptor;

@Getter
@Setter
@ToString
public class GeneralMessageDTO {
    Integer code;
    String message;

    public static GeneralMessageDTO getInvalidSession(){
        GeneralMessageDTO generalMessageDTO = new GeneralMessageDTO();
        generalMessageDTO.setCode(405);
        generalMessageDTO.setMessage("Session was not found.");
        return generalMessageDTO;
    }

    public static GeneralMessageDTO getMessage(int code, String message){
        GeneralMessageDTO generalMessageDTO = new GeneralMessageDTO();
        generalMessageDTO.setCode(code);
        generalMessageDTO.setMessage(message);
        return generalMessageDTO;
    }
}
