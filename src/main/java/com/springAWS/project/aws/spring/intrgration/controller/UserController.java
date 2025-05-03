package com.springAWS.project.aws.spring.intrgration.controller;


import com.springAWS.project.aws.spring.intrgration.model.User;
import com.springAWS.project.aws.spring.intrgration.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

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
