package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.messages.RegisterRequest;
import com.github.nightdeveloper.smartdashboard.messages.RegisterMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@Slf4j
public class CamerasWebsocketController {

    @MessageMapping(Constants.MESSAGE_MAPPING_REGISTER)
    @SendTo(Constants.TOPIC_REGISTER)
    public RegisterMessage register(RegisterRequest request, SimpMessageHeaderAccessor headers)  {
        log.info("register request " + request);
        return new RegisterMessage(headers.getSessionId(), "Registered " + request.getId());
    }

    @GetMapping(value = Constants.ENDPOINT_WSTEST)
    @ResponseBody
    public ModelAndView wsTest(Principal principal) {
        log.info("wstest frontend requested by " + principal.getName());
        return new ModelAndView("wstest");
    }
}
