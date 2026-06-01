package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleBusinessValidation(
            BusinessValidationException ex,
            HttpServletRequest request
    ) {
        return buildValidationResponse(ex.getMessage(), ex.getFields(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DocumentoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoNoEncontrado(
            DocumentoNoEncontradoException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DocumentoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoInvalido(
            DocumentoInvalidoException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(
            ExternalApiException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible consultar el servicio externo de DNI/RUC. Intente nuevamente.",
                request
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tienes permisos para acceder a este recurso.", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                buildDataIntegrityMessage(ex),
                request
        );
    }

    @ExceptionHandler({EntityNotFoundException.class, JpaObjectRetrievalFailureException.class})
    public ResponseEntity<ErrorResponse> handleEntityReferenceNotFound(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "La operacion referencia un registro relacionado que no existe.",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            addFieldError(fields, error.getField(), normalizeValidationMessage(error.getDefaultMessage()));
        }
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                addFieldError(fields, error.getObjectName(), normalizeValidationMessage(error.getDefaultMessage()))
        );

        return buildValidationResponse(
                "Datos de entrada invalidos.",
                fields,
                request
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodValidation(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        ex.getParameterValidationResults().forEach(result -> {
            String field = result.getMethodParameter().getParameterName();
            if (field == null || field.isBlank()) {
                field = "parametro";
            }
            final String fieldName = field;
            result.getResolvableErrors().forEach(error ->
                    addFieldError(fields, fieldName, normalizeValidationMessage(error.getDefaultMessage()))
            );
        });

        return buildValidationResponse("Parametros de entrada invalidos.", fields, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                addFieldError(
                        fields,
                        extractFieldName(violation.getPropertyPath().toString()),
                        normalizeValidationMessage(violation.getMessage())
                )
        );

        return buildValidationResponse("Parametros de entrada invalidos.", fields, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        String requiredType = ex.getRequiredType() == null ? "valido" : ex.getRequiredType().getSimpleName();
        addFieldError(
                fields,
                ex.getName(),
                "Debe tener un valor de tipo " + translateType(requiredType) + "."
        );

        return buildValidationResponse("Parametros de entrada invalidos.", fields, request);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormat(NumberFormatException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El identificador numerico no tiene un formato valido.", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El cuerpo JSON esta mal formado o contiene tipos invalidos.", request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ValidationErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        addFieldError(fields, ex.getParameterName(), "El parametro es obligatorio.");
        return buildValidationResponse("Parametros de entrada invalidos.", fields, request);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleRequestBinding(
            ServletRequestBindingException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, "La solicitud no contiene los datos requeridos.", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        HttpHeaders headers = new HttpHeaders();
        if (ex.getSupportedHttpMethods() != null) {
            headers.setAllow(ex.getSupportedHttpMethods());
        }

        ErrorResponse body = buildBody(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Metodo HTTP no permitido para este recurso.",
                request
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        LOGGER.error("Error no controlado en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor.", request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    private ErrorResponse buildBody(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    private ResponseEntity<ValidationErrorResponse> buildValidationResponse(
            String message,
            Map<String, List<String>> fields,
            HttpServletRequest request
    ) {
        ValidationErrorResponse body = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fields
        );
        return ResponseEntity.badRequest().body(body);
    }

    private void addFieldError(Map<String, List<String>> fields, String field, String message) {
        fields.computeIfAbsent(field, ignored -> new ArrayList<>()).add(message);
    }

    private String extractFieldName(String propertyPath) {
        int lastDot = propertyPath.lastIndexOf('.');
        if (lastDot >= 0 && lastDot + 1 < propertyPath.length()) {
            return propertyPath.substring(lastDot + 1);
        }
        return propertyPath;
    }

    private String normalizeValidationMessage(String message) {
        if (message == null || message.isBlank()) {
            return "No cumple la regla de validacion.";
        }

        return switch (message) {
            case "must not be blank" -> "No debe estar vacio.";
            case "must not be null" -> "Es obligatorio.";
            case "must be a well-formed email address" -> "Debe tener un formato de correo valido.";
            case "must be greater than or equal to 0" -> "Debe ser mayor o igual a 0.";
            case "must be greater than or equal to 1" -> "Debe ser mayor o igual a 1.";
            default -> normalizeParameterizedMessage(message);
        };
    }

    private String normalizeParameterizedMessage(String message) {
        if (message.startsWith("size must be between")) {
            return "La longitud no esta dentro del rango permitido.";
        }
        if (message.startsWith("must match")) {
            return "No cumple el formato requerido.";
        }
        if (message.startsWith("must be greater than or equal to")) {
            return "Debe ser mayor o igual al valor minimo permitido.";
        }
        return message;
    }

    private String translateType(String requiredType) {
        return switch (requiredType) {
            case "Integer", "Long" -> "numero entero";
            case "BigDecimal", "Double", "Float" -> "numero decimal";
            case "Boolean" -> "booleano";
            default -> requiredType;
        };
    }

    private String buildDataIntegrityMessage(DataIntegrityViolationException ex) {
        Throwable root = ex.getMostSpecificCause();
        String detail = root == null ? "" : root.getMessage();
        String normalized = detail == null ? "" : detail.toLowerCase();

        if (normalized.contains("foreign key")) {
            return "La operacion referencia un registro relacionado que no existe o esta en uso.";
        }
        if (normalized.contains("duplicate")) {
            return "Ya existe un registro con uno de los valores enviados.";
        }
        if (normalized.contains("cannot be null") || normalized.contains("not null")) {
            return "Faltan datos obligatorios para guardar el registro.";
        }
        return "La operacion no cumple las restricciones de la base de datos.";
    }
}
