package com.springAWS.project.aws.spring.intrgration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


@Setter
@Getter
@Entity(name = "file")
public class File {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Short fileId;

    @Column(name="file_name")
    private String fileName;

    @Column(name = "file_type")
    private Short fileType;

    @Column(name = "file_data")
    String fileData;

    @Column(name = "upload_date")
    private LocalTime uploadDate;

    @Column(name = "uploaded_user_id")
    private Integer userId;

}
