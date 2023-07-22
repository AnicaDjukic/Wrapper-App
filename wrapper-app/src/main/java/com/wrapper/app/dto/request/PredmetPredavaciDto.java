package com.wrapper.app.dto.request;

import com.wrapper.app.dto.request.PredavaciDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredmetPredavaciDto extends PredavaciDto {

    @NotNull
    @NotBlank
    private String predmetId;
}
