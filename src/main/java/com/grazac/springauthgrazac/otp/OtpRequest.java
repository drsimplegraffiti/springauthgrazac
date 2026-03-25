package com.grazac.springauthgrazac.otp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpRequest {
    private String email;
    private String otpCode;
    private String purpose;;

}
