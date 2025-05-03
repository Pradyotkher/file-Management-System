package com.springAWS.project.aws.spring.intrgration.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity(name = "file_logs")
@Getter
@Setter
public class FileLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "operation_date")
    private LocalTime operationDate;

    @Column(name = "operation_type")
    private Short operationType;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "operation_outcome")
    private Short operationOutcome;



}
