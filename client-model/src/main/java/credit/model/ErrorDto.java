package credit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Error")
public class ErrorDto {

    @JsonProperty("message")
    @ApiModelProperty(example = "Error message",
                      required = true)
    private String message;

    public ErrorDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
