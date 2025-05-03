package com.springAWS.project.aws.spring.intrgration.jpa;

import com.springAWS.project.aws.spring.intrgration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<User , Integer> {
}
