package com.springAWS.project.aws.spring.intrgration.serviceImpl;

import com.springAWS.project.aws.spring.intrgration.constants.Messages;
import com.springAWS.project.aws.spring.intrgration.jpa.UserJPARepository;
import com.springAWS.project.aws.spring.intrgration.model.User;
import com.springAWS.project.aws.spring.intrgration.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserJPARepository userJPARepository;

    @Override
    @Transactional
    public void createUser(User user, Short userType) throws Exception {

        LocalDate date = LocalDate.now();
        user.setUserType(userType);
        // logic to check the password

        // logic to check if the userName already exists
        User dbUser = getUserByUserName(user.getUserName());
        if(dbUser!=null){
            throw new Exception(Messages.USER_ALREADY_EXISTS.getMessage());
        }

        // check if registration date is null or not
        if(user.getRegistrationDate()==null){
            user.setRegistrationDate(date);
        }

        userJPARepository.save(user);
    }

    @Override
    public void updateUser(User user) throws Exception {
        User dbUser = getUserByUserName(user.getUserName());
        if(dbUser==null){
                throw new Exception(Messages.CANNOT_FIND_USER.getMessage());
        }
        userJPARepository.saveAndFlush(user);
    }

    @Override
    public void deleteUserByUserName(String userName) throws Exception {
        User dbUser = getUserByUserName(userName);
        if(dbUser==null){
            throw new Exception(Messages.CANNOT_FIND_USER.getMessage().concat(userName));
        }
        userJPARepository.delete(dbUser);
    }

    @Override
    public User getUserByUserName(String userName) {
        Collection<User> users = userJPARepository.findAll();
        if(users.isEmpty()){
            return null;
        }
        if(users.stream().anyMatch(user -> user.getUserName().equals(userName)))
            return users.stream().filter(user -> user.getUserName().equals(userName)).findFirst().get();
        return null;
    }
}
