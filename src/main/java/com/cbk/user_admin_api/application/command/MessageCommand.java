package com.cbk.user_admin_api.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class MessageCommand {
    private int ageGroup;
    private String message;
}
