package com.springAWS.project.aws.spring.intrgration.service;

import com.springAWS.project.aws.spring.intrgration.model.File;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public interface FileService {

    // Upload a file
    void uploadFile(String bucketNane , String keyName , Long contentLength , InputStream value , String contentType , byte[] byteArray , String userName) throws Exception;

    // Download a file
    void downloadFile(String bucketName , String keyName,  String userName) throws Exception;

    // delete a file
    void deleteFile(String bucketName , String keyName , String userName) throws Exception;

    // get All files from the Amazon S3 Bucket
    List<String> listOfFiles(String bucketName);

    public short getMaxFileIdFromDB();

    public File getFileByFileName(String fileName) throws ParseException;
}
