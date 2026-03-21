package com.grazac.springauthgrazac.teacher;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder @Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "tbl_wallet")
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String subject;
    private Integer age;
    private Double salary;
    private LocalDate createdAt;
}
