package com.socialmedia.api.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.socialmedia.api.dto.request.PostCreateRequest;
import com.socialmedia.api.model.validation.order.FirstOrder;
import com.socialmedia.api.model.validation.order.FourthOrder;
import com.socialmedia.api.model.validation.order.SecondOrder;
import com.socialmedia.api.model.validation.order.ThirdOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@GroupSequence({PostUpdateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class PostUpdateRequest extends PostCreateRequest {

    @NotNull(message = "post id must be provided", groups = FirstOrder.class)
    @Min(value = 1, message = "Invalid Post Id", groups = SecondOrder.class)
    private Long id;

}
