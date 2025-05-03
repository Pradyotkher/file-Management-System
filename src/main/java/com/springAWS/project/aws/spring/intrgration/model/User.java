package com.springAWS.project.aws.spring.intrgration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Entity(name = "user_details")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type")
    private Short userType;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
