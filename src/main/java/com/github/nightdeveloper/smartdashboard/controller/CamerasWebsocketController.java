package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.messages.RegisterRequest;
import com.github.nightdeveloper.smartdashboard.messages.RegisterResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CamerasWebsocketController {

    @MessageMapping("/ws/register")
    @SendTo("/topic/register")
    public RegisterResponse register(RegisterRequest message) throws Exception {
        return new RegisterResponse("Registered " + message.getId());
    }
}
