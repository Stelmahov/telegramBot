package by.resliv.telegramBot.handlers;

import by.resliv.telegramBot.dto.ApiErrorDto;
import by.resliv.telegramBot.dto.FieldErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleBadRequest(final BadRequestException exception) {
        return new ApiErrorDto(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleNotFount(final NotFoundException exception) {
        return new ApiErrorDto(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleMethodArgumentError(final MethodArgumentNotValidException exception) {
        List<FieldErrorDto> errorFields = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ApiErrorDto("Bad request", errorFields);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ApiErrorDto("Error!");
    }
}
