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
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@GroupSequence({CommentUpdateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class CommentUpdateRequest {

    @NotNull(message = "comment id must be provided", groups = FirstOrder.class)
    @Min(value = 1, message = "Invalid Comment Id", groups = SecondOrder.class)
    private Long id;

    @NotBlank(message = "Content is blank", groups = FirstOrder.class)
    @Size(max = 200, message = "Content is over 200 characters", groups = SecondOrder.class)
    private String content;


}
