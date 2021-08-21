package com.github.nightdeveloper.smartdashboard.messages;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageRequest {
    private String position;
    private String file;
}
