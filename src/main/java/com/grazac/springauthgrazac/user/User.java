package com.grazac.springauthgrazac.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "application_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private Boolean isVerified;

//    @JsonIgnore
    private String username; // internal use alone
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;
}
