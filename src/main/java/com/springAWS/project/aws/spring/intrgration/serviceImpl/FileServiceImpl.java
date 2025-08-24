package com.springAWS.project.aws.spring.intrgration.serviceImpl;

import com.springAWS.project.aws.spring.intrgration.constants.FileType;
import com.springAWS.project.aws.spring.intrgration.constants.Messages;
import com.springAWS.project.aws.spring.intrgration.constants.OperationType;
import com.springAWS.project.aws.spring.intrgration.constants.UserType;
import com.springAWS.project.aws.spring.intrgration.exceptions.FileException;
import com.springAWS.project.aws.spring.intrgration.jpa.FileJPARepository;

import com.springAWS.project.aws.spring.intrgration.jpa.FileLogsJPARepository;
import com.springAWS.project.aws.spring.intrgration.model.FileLogs;
import com.springAWS.project.aws.spring.intrgration.model.RequiresRoles;
import com.springAWS.project.aws.spring.intrgration.model.User;
import com.springAWS.project.aws.spring.intrgration.service.FileService;
import com.springAWS.project.aws.spring.intrgration.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import com.springAWS.project.aws.spring.intrgration.model.File;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final String userHome = System.getProperty("user.home");

    private final String DOWNLOADS_FILE_PATH = userHome+"/Downloads/awsDownload_";

    private final String PDF_CONTENT_TYPE = "application/pdf";

    private final String s3Path = "s3://";

    @Autowired
    private S3Client s3Client;

    @Autowired
    private FileJPARepository fileJPARepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private FileLogsJPARepository fileLogsJPARepository;

    @Override
    @Transactional
    @RequiresRoles({UserType.ADMIN , UserType.EDITOR})
    // Add Logic for Role checking in Authentication Aspect File
    public void uploadFile(String bucketNane, String keyName, Long contentLength, InputStream value , String contentType , byte[] byteArray , String userName) throws Exception {

        User currentUser = userService.getUserByUserName(userName);
        if (currentUser == null) {
            throw new FileException(Messages.CANNOT_FIND_USER.getMessage().concat(userName));
        }

        try{
            PutObjectRequest request = PutObjectRequest.builder().key(keyName).bucket(bucketNane).contentType(contentType).build();
            s3Client.putObject(request, RequestBody.fromInputStream(value, contentLength));
            log.info(Messages.FILE_UPLOAD_SUCCESSFUL.getMessage());
            saveToDb(keyName, contentType, byteArray, bucketNane, currentUser);
            saveFileLogs(keyName, currentUser, OperationType.UPLOAD , 1);
        }catch (Exception e){
            saveFileLogs(keyName , currentUser , OperationType.UPLOAD , 0);
            throw new Exception(e.getMessage());
        }
    }


    @Override
    @RequiresRoles({UserType.ADMIN , UserType.EDITOR,  UserType.VIEWER})
    public void downloadFile(String bucketName, String keyName ,String userName ) throws Exception {

        User currentUser = userService.getUserByUserName(userName);

        if (currentUser == null) {
            throw new FileException(Messages.CANNOT_FIND_USER.getMessage().concat(userName));
        }
        try {
            File keyNameFile = getFileByFileName(keyName);
            GetObjectRequest request = GetObjectRequest.builder().key(keyNameFile.getFileName()).bucket(bucketName).build();
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            String contentType = response.response().contentType();
            byte[] result = response.readAllBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(result);
            savePDF(outputStream, contentType, keyName);
            saveFileLogs(keyName, currentUser, OperationType.DOWNLOAD , 1);
        }catch (Exception e){
            saveFileLogs(keyName, currentUser, OperationType.DOWNLOAD , 0);
            throw new Exception(e.getMessage());
        }
    }

    private byte[] getFileByteArrayFromAWS(String keyName , String bucketName) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder().key(keyName).bucket(bucketName).build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        String contentType = response.response().contentType();
        return response.readAllBytes();
    }

    private void savePDF(ByteArrayOutputStream outputStream , String contentType , String fileName){
        try(FileOutputStream fileOutputStream = new FileOutputStream(getFilePath(contentType , fileName))){
            outputStream.writeTo(fileOutputStream);
            log.info(Messages.FILE_SAVED_TO_DOWNLOADS.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilePath(String contentType , String fileName){
        log.info(contentType);
        if(contentType.equals(PDF_CONTENT_TYPE)){
            return  DOWNLOADS_FILE_PATH.concat(fileName).concat(".pdf");
        }
        else{
            return DOWNLOADS_FILE_PATH+".png";
        }
    }

    @Override
    @RequiresRoles({UserType.ADMIN})
    // Add Logic for Role checking in Authentication Aspect File
    public void deleteFile(String bucketName, String keyName , String userName) throws Exception {
        User currentUser = userService.getUserByUserName(userName);
        if(currentUser == null){
            throw new RuntimeException(Messages.CANNOT_FIND_USER.getMessage());
        }

        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
            s3Client.deleteObject(request);
            log.info(Messages.FILE_DELETE_SUCCESSFUL.getMessage());
            saveFileLogs(keyName, currentUser, OperationType.DELETE , 1);
        }
        catch (Exception e){
            saveFileLogs(keyName , currentUser , OperationType.DELETE , 0);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<String> listOfFiles(String bucketName) {
        return List.of();
    }

    @Override
    public short getMaxFileIdFromDB() {
        String query = "SELECT MAX(f.id)  from file f";
        Short i = (Short)entityManager.createQuery(query).getSingleResult();
        return i!=null ? i : 0;
    }

    @Override
    public File getFileByFileName(String fileName) throws ParseException {
        Collection<File> allFiles = fileJPARepository.findAll();
       return  allFiles.stream().filter(file -> file.getFileName().contains(fileName)).findFirst().get();
    }

    private void saveToDb(String fileName , String contentType , byte[] byteArray , String bucketName , User currentUser) throws Exception {
        try {
            File newEntity = new File();
            short fileType = getFileType(contentType);
            String fileData = s3Path.concat(bucketName).concat("/").concat(fileName);
            newEntity.setFileName(fileName);
            newEntity.setFileData(fileData);
            newEntity.setFileType(fileType);
            newEntity.setUploadDate(LocalTime.now());
            newEntity.setUserId(currentUser.getUserId());
            fileJPARepository.save(newEntity);
            log.info("File successfully saved to DB");


        } catch (Exception e){
            if(getFileByteArrayFromAWS(fileName , bucketName).length!=0){
                log.info(Messages.DELETE_FILE_FAILED.getMessage());
            }
        }
    }

    private void saveFileLogs(String fileName , User currentUser , OperationType operationType , Integer operationOutcome){
        // Save the file logs as well now
        try {
            FileLogs fileLogs = new FileLogs();
            fileLogs.setFileName(fileName);
            fileLogs.setUserId(currentUser.getUserId());
            fileLogs.setUserName(currentUser.getUserName());
            fileLogs.setOperationType(operationType.getOperationType().shortValue());
            fileLogs.setOperationDate(LocalTime.now());
            fileLogs.setOperationOutcome(operationOutcome.shortValue());
            fileLogsJPARepository.save(fileLogs);
            log.info("File logs updated");
        } catch (Exception e){
            log.error("Error occurred while saving the logs");
        }
    }

    private short getFileType(String contentType){
        if(contentType.equals("application/pdf")){
            return (short)FileType.PDF.getType();
        }
        else {
            return (short) FileType.PNG.getType();
        }
    }

}
