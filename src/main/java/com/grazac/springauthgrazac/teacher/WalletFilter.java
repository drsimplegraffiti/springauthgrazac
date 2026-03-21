package com.grazac.springauthgrazac.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletFilter {
    private String name;
    private String subject;

    private Integer age;
    private Double salary;
    private LocalDate createdAt;
}
