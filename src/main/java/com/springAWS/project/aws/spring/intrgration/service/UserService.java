package com.springAWS.project.aws.spring.intrgration.service;

import com.springAWS.project.aws.spring.intrgration.model.User;

public interface UserService {

    void createUser(User user, Short userType) throws  Exception;

    void updateUser(User user) throws Exception;

    void deleteUserByUserName(String userName) throws Exception;

    User getUserByUserName(String userName);

    String getUserRole(String userName);
}
