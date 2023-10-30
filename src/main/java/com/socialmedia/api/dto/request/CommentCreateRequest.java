package com.socialmedia.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@GroupSequence({CommentCreateRequest.class, FirstOrder.class,
        SecondOrder.class, ThirdOrder.class, FourthOrder.class})
public class CommentCreateRequest {

    @NotNull(message = "post id must be provided", groups = FirstOrder.class)
    @Min(value = 1, message = "Invalid Post Id", groups = SecondOrder.class)
    private Long postId;

    @JsonProperty("comment")
    @NotBlank(message = "Comment is blank", groups = FirstOrder.class)
    @Size(max = 200, message = "Comment is over 200 characters", groups = SecondOrder.class)
    private String content;


}
