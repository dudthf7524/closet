package com.project.closet.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private String alert;
    private String message;              // 사용자에게 전달할 메시지
    private String redirectUri;          // 리다이렉트 URI


}
