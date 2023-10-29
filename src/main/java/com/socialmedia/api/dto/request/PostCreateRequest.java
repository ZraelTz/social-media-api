package com.socialmedia.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.socialmedia.api.model.validation.order.FirstOrder;
import com.socialmedia.api.model.validation.order.FourthOrder;
import com.socialmedia.api.model.validation.order.SecondOrder;
import com.socialmedia.api.model.validation.order.ThirdOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@GroupSequence({PostCreateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class PostCreateRequest {

    @NotBlank(message = "Content is blank", groups = FirstOrder.class)
    @Size(max = 250, message = "Content is over 250 characters", groups = SecondOrder.class)
    private String content;
}
