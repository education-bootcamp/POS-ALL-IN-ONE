package com.devstack.POS.advisor;

import com.devstack.POS.exception.DuplicateEntryException;
import com.devstack.POS.exception.EntryNotFoundException;
import com.devstack.POS.exception.ValidationException;
import com.devstack.POS.util.StandardResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWiderExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponseDTO> handleEntryNotFoundException(EntryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponseDTO.builder()
                        .code(404)
                        .message(ex.getMessage())
                        .data(ex)
                        .build());
    }
    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<StandardResponseDTO> handleEntryDuplicateEntryException(DuplicateEntryException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(StandardResponseDTO.builder()
                        .code(409)
                        .message(ex.getMessage())
                        .data(ex)
                        .build());
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardResponseDTO> handleValidationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(StandardResponseDTO.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .data(ex)
                        .build());
    }
}
