package com.springAWS.project.aws.spring.intrgration.controller;

import com.springAWS.project.aws.spring.intrgration.constants.Messages;
import com.springAWS.project.aws.spring.intrgration.model.File;
import com.springAWS.project.aws.spring.intrgration.service.FileService;
import com.springAWS.project.aws.spring.intrgration.serviceImpl.FileServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    FileService fileService;


    @PostMapping("/{bucketName}/uploaad")
    public ResponseEntity<?> uploaadFile(@PathVariable("bucketName") String bucketName , @RequestParam("file") MultipartFile file , @RequestParam("userName") String userName) throws Exception {
        try {
            if (file.isEmpty()) {

                return ResponseEntity.badRequest().body(Messages.EMPTY_FILE.getMessage());
            }
            log.info(userName);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            Long size = file.getSize();
            InputStream stream = file.getInputStream();
            byte[] byteArray = file.getBytes();
            fileService.uploadFile(bucketName, fileName, size, stream, contentType, byteArray , userName);
            return ResponseEntity.ok(Messages.FILE_UPLOAD_SUCCESSFUL.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{bucketName}/download")
    public ResponseEntity<?> downloadFile(@PathVariable("bucketName") String bucketName , @RequestParam("fileName") String fileName , @RequestParam("userName")String userName) throws Exception {
        try {
            fileService.downloadFile(bucketName, fileName, userName);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName: " + fileName).body(Messages.FILE_SAVED_TO_DOWNLOADS.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{bucketName}/delete")
    public ResponseEntity<?> deleteFile(@PathVariable("bucketName") String bucketName , @RequestParam("file") String fileName , @RequestParam("userName") String userName) throws Exception {
        try {
            fileService.deleteFile(bucketName, fileName, userName);
            return ResponseEntity.ok(Messages.FILE_DELETE_SUCCESSFUL.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getFileByFileName/{fileName}")
    public File getFileFromDb(@PathVariable("fileName") String fileName) throws ParseException {
        return fileService.getFileByFileName(fileName);
    }

    @GetMapping("/getLastFileId")
    public Short getLastFileId(){
        return fileService.getMaxFileIdFromDB();
    }


}
