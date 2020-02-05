package com.example.task.repository;


import com.example.task.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long>, JpaSpecificationExecutor<Task> {

  List<Task> findAll(Specification<Task> task, Sort sort);
}
