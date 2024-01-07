package com.dane.homework_help.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    public String accesstoken;
    public String refreshtoken;
    public String confirmToken;
    public String error;
}
