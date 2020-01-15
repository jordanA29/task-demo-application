package com.sonalake.refactoring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    public Long id;
    public String name;

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }
}
