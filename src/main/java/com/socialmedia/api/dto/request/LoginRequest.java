package com.socialmedia.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    @NotBlank(message = "Username is blank")
    private String username;

    @NotBlank(message = "Password is blank")
    private String password;
}
