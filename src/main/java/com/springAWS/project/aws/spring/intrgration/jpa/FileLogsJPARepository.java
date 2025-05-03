package com.springAWS.project.aws.spring.intrgration.jpa;

import com.springAWS.project.aws.spring.intrgration.model.FileLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileLogsJPARepository extends JpaRepository<FileLogs , Integer> {
}
