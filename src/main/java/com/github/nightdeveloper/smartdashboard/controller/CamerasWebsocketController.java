package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.exception.WebsocketException;
import com.github.nightdeveloper.smartdashboard.messages.ImageMessage;
import com.github.nightdeveloper.smartdashboard.messages.ImageRequest;
import com.github.nightdeveloper.smartdashboard.messages.RegisterRequest;
import com.github.nightdeveloper.smartdashboard.messages.RegisterMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@Controller
@Slf4j
public class CamerasWebsocketController {

    @MessageMapping(Constants.MESSAGE_MAPPING_REGISTER)
    @SendTo(Constants.TOPIC_REGISTER)
    public RegisterMessage register(RegisterRequest request, SimpMessageHeaderAccessor headers)  {
        log.info("register request " + request);
        return new RegisterMessage(headers.getSessionId(), "Registered " + request.getId());
    }

    @MessageMapping(Constants.MESSAGE_MAPPING_IMAGE)
    @SendTo(Constants.TOPIC_IMAGE)
    public ImageMessage image(ImageRequest request, SimpMessageHeaderAccessor headers)
            throws WebsocketException, IOException {
        log.info("image request " + request);

        String fileStr = request.getFile();
        String[] splitted = fileStr.split("base64,");

        log.info(splitted[0]);

        if (splitted.length != 2) {
            log.error("invalid image request");
            throw new WebsocketException("invalid image request");
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(splitted[1]);
            FileUtils.writeByteArrayToFile(new File("x:/testimage.jpg"), decodedBytes);

            return new ImageMessage(headers.getSessionId(), request.getPosition(),
                    decodedBytes);
            
        } catch(IllegalArgumentException e) {
            log.error("invalid base64 text", e);
            throw new WebsocketException("Invalid incoming image");
        }
    }

    @GetMapping(value = Constants.ENDPOINT_WSTEST)
    @ResponseBody
    public ModelAndView wsTest(Principal principal) {
        log.info("wstest frontend requested by " + principal.getName());
        return new ModelAndView("wstest");
    }
}
