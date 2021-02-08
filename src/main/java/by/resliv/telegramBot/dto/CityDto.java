package by.resliv.telegramBot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Integer id;
    @NotBlank
    @NotNull
    private String name;
    private String info;
}
