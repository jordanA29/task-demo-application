package com.example.task.web.controller;

import com.example.task.web.exception.TaskNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {


  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleTodoNotFoundException(TaskNotFoundException ex) {
    System.out.println("404 not found "+ ex);
  }

  @Override
  protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    FieldError fieldError = ex.getBindingResult().getFieldError();

    String message = fieldError.getDefaultMessage();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", message).build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<Object> handleConstraintViolationException(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", ex.getMessage()).build();
  }

}
