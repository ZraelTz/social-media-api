package com.socialmedia.api.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.socialmedia.api.model.validation.order.FirstOrder;
import com.socialmedia.api.model.validation.order.FourthOrder;
import com.socialmedia.api.model.validation.order.SecondOrder;
import com.socialmedia.api.model.validation.order.ThirdOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@GroupSequence({UserFollowingUpdateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class UserFollowingUpdateRequest {

    @NotBlank(message = "Username is blank", groups = FirstOrder.class)
    private String username;

    @NotNull(message = "'follow' boolean must be provided")
    private Boolean follow;

}
