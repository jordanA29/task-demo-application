package com.sonalake.refactoring.web.specification;

import com.sonalake.refactoring.model.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class TaskFilter implements Specification<Task> {

  private String user;
  private String dateAfter;
  private String includeCompleted;
  @Override
  public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    ArrayList<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(user))
    {
      predicates.add(cb.equal(root.get("user").get("name"), user));
    }
    if (StringUtils.isNotBlank(dateAfter))
    {
      predicates.add(cb.greaterThan(root.get("due"), dateAfter));
    }
    if (StringUtils.isBlank(includeCompleted))
    {
      predicates.add(cb.equal(root.get("isCompleted"), false) );
    }
    if (StringUtils.isNotBlank(includeCompleted) && Boolean.parseBoolean(includeCompleted) == false)
    {
      predicates.add(cb.equal(root.get("isCompleted"), includeCompleted));
    }

    return predicates.size() <= 0 ? null : cb.and(predicates.toArray(new Predicate[predicates.size()]));
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getDateAfter() {
    return dateAfter;
  }

  public void setDateAfter(String dateAfter) {
    this.dateAfter = dateAfter;
  }

  public String getIncludeCompleted() {
    return includeCompleted;
  }

  public void setIncludeCompleted(String includeCompleted) {
    this.includeCompleted = includeCompleted;
  }

}
