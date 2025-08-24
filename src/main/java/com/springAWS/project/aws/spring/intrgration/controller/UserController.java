package com.springAWS.project.aws.spring.intrgration.controller;


import com.springAWS.project.aws.spring.intrgration.model.User;
import com.springAWS.project.aws.spring.intrgration.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    private ListableBeanFactory beanFactory;

    @PostConstruct
    public void debugBeans() {
        String[] beanNames = beanFactory.getBeanNamesForType(UserService.class);
        for (String name : beanNames) {
            Object bean = beanFactory.getBean(name);
            log.info("{} -> {}", name, bean.getClass().getName());
        }
        log.info("Injected userService instance: {}", userService.getClass().getName());
    }

    @PostConstruct
    public void listMethods() {
        for (var method : userService.getClass().getMethods()) {
            if (method.getDeclaringClass().getName().contains("UserServiceImpl")) {
                System.out.println("Method in UserServiceImpl: " + method.getName());
            }
        }
    }

    @PutMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user , @RequestParam("userType")Short userType) throws  Exception{
        try {
            userService.createUser(user, userType);
            return ResponseEntity.ok("User " + user.getUserName() + " created Successfully");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) throws Exception{
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("User ".concat(user.getUserName()).concat(" Updated Successfully"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam("userName") String userName) throws Exception{
        try {
            userService.deleteUserByUserName(userName);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
