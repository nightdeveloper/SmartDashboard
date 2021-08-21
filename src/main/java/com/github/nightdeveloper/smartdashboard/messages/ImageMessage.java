package com.github.nightdeveloper.smartdashboard.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ImageMessage {
    private String sessionId;
    private String position;
    private byte[] file;
}