package com.springAWS.project.aws.spring.intrgration.jpa;

import com.springAWS.project.aws.spring.intrgration.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJPARepository extends JpaRepository<File, Short> {
}
