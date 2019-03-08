package com.sonalake.refactoring;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue
    public Long id;
    public Long userId;
    public String task;
    public String due;
    public boolean isCompleted;
}