package com.github.nightdeveloper.smartdashboard.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterMessage {
    private String sessionId;
    private String message;
}
