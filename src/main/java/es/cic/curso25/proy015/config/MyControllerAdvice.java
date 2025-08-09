package es.cic.curso25.proy015.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.exception.PlazaOcupadaException;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Recurso no encontrado: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Solicitud incorrecta: " + ex.getMessage());
    }
    @ExceptionHandler(PlazaOcupadaException.class)
    public ResponseEntity<String> handlePlazaOcupada(PlazaOcupadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}

