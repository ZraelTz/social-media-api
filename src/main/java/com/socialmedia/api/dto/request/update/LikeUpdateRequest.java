package com.socialmedia.api.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.socialmedia.api.model.validation.order.FirstOrder;
import com.socialmedia.api.model.validation.order.FourthOrder;
import com.socialmedia.api.model.validation.order.SecondOrder;
import com.socialmedia.api.model.validation.order.ThirdOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@GroupSequence({LikeUpdateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class LikeUpdateRequest {

    @NotNull(message = "like id must be provided", groups = FirstOrder.class)
    @Min(value = 1, message = "Invalid Like Id", groups = SecondOrder.class)
    private Long id;

    @NotNull(message = "'like' boolean must be provided")
    private Boolean like;

}
