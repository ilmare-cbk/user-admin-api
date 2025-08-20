package com.cbk.user_admin_api.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Message {
    private String name;
    private String phone;
    private String message;
}
