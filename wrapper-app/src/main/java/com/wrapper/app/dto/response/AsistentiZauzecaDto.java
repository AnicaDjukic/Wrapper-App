package com.wrapper.app.dto.response;

import com.wrapper.app.dto.response.PredavacDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AsistentiZauzecaDto {

    private PredavacDto asistent;
    private int brojTermina;
}
