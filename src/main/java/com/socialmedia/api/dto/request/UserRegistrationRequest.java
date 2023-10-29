package com.socialmedia.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.socialmedia.api.model.validation.FieldsValueMatch;
import com.socialmedia.api.model.validation.PasswordChecker;
import com.socialmedia.api.model.validation.order.FirstOrder;
import com.socialmedia.api.model.validation.order.FourthOrder;
import com.socialmedia.api.model.validation.order.SecondOrder;
import com.socialmedia.api.model.validation.order.ThirdOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.socialmedia.api.model.Constant.EMAIL_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "password", fieldMatch = "confirmPassword",
                message = "Password and Confirm Password do not match",
                groups = FourthOrder.class)})
@GroupSequence({UserRegistrationRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class UserRegistrationRequest {

    @NotBlank(message = "Username is blank", groups = FirstOrder.class)
    @Size(max = 50, message = "Username is over 50 characters", groups = SecondOrder.class)
    private String username;

    @NotBlank(message = "Email is blank", groups = FirstOrder.class)
    @Size(max = 100, message = "Username is over 100 characters", groups = SecondOrder.class)
    @Email(message = "Email is invalid", regexp = EMAIL_REGEX, groups = ThirdOrder.class)
    private String email;

    @NotBlank(message = "Password is blank", groups = FirstOrder.class)
    @PasswordChecker(groups = SecondOrder.class)
    private String password;

    @NotBlank(message = "Confirm Password is blank")
    private String confirmPassword;
}
