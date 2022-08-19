package me.study.userservice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninResDto {
    @NotNull
    @Size(min = 3, max = 100)
    private String email;

}
