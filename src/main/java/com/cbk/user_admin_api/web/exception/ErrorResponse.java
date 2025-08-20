package com.cbk.user_admin_api.web.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
    private int status;
    private String message;
}
