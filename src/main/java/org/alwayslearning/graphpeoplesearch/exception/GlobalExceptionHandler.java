package org.alwayslearning.graphpeoplesearch.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

  //Custom exception handler for ResourceNotFoundException.java
  @ExceptionHandler(value=ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
    log.error(HttpStatus.NOT_FOUND + ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
        ex.getMessage()));
  }

  //Custom exception handler for ResourceNotFoundException.java
  @ExceptionHandler(value=UnauthorizedAccessException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex){
    log.error(HttpStatus.UNAUTHORIZED + ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
        ex.getMessage()));
  }

  //Check validations if you add validation rules on DTO class
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<String> errorMessages = ex.getBindingResult().getFieldErrors()
        .stream().map(FieldError::getDefaultMessage).toList();
    log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        errorMessages.toString()));
  }


  //Check validations if you add validation rules on entity class
  @ExceptionHandler(value=ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex){
    log.error(HttpStatus.BAD_REQUEST + ex.getMessage());
    List<String> errorMessages = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errorMessages.add(violation.getMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        errorMessages.toString()));
  }

  //When given wrong request param
  @ExceptionHandler(value= MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
    log.error(HttpStatus.METHOD_NOT_ALLOWED + ex.getMessage());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(),
        ex.getMessage()));
  }

  //When given invalid request method
  @ExceptionHandler(value= HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
    log.error(HttpStatus.METHOD_NOT_ALLOWED + ex.getMessage());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(),
        ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error(HttpStatus.BAD_REQUEST+ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        ex.getMessage()));
  }

  //When given invalid values to request body
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.error(HttpStatus.BAD_REQUEST+ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        ex.getMessage()));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    log.error(HttpStatus.NOT_FOUND + ex.getRequestURL());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getRequestURL()));
  }

  //Invalid media types
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex) {
    log.error(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE), ex.getContentType());
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getContentType().toString()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleCommonException(Exception ex){
    log.error(ex.getMessage(), ex);
    if (ex.getMessage().contains(HttpStatus.UNAUTHORIZED.getReasonPhrase()) ||
        ex.getMessage().contains("Required request header 'Authorization'")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
          ex.getMessage()));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
  }
}

