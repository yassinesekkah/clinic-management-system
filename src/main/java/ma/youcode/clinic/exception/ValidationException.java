package ma.youcode.clinic.exception;

import java.util.Collections;
import java.util.List;

/**
 * Exception thrown when business or input validation constraints fail.
 * Holds an accumulated list of all validation error messages to provide
 * complete feedback to the user interface.
 */
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public ValidationException(List<String> errors) {
        super(errors != null && !errors.isEmpty() ? String.join(", ", errors) : "Validation failed");
        this.errors = errors != null ? List.copyOf(errors) : Collections.emptyList();
    }

    public List<String> getErrors() {
        return errors;
    }
}
