package by.resliv.telegramBot.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@AllArgsConstructor
public class ApiErrorDto {
    String message;
    List<FieldErrorDto> fields;

    public ApiErrorDto(final String message) {
        this.message = message;
        this.fields = Collections.emptyList();
    }
}
