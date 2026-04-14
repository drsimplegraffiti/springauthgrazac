package com.grazac.springauthgrazac.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountCreateRequest {
    private String accountNumber;
    private String accountName;
    private Long userId;
}
