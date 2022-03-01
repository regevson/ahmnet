package at.ahmacademy.ahmnet.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleException(AccessDeniedException e) {
	e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Fuer diese Aktion hast du zu wenig Rechte.");
    }   

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
	e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Bei der Abfrage ist etwas schiefgegangen!\n"
                	+ "Bitte schicken Sie dem Admin folgende Fehlermeldung:\n\n\n" + exAsString(e));
    }   

    private String exAsString(Exception e) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	return sw.toString().substring(0, 400) + "...";
    }

}
