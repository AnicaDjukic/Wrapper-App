package com.wrapper.app.dto;

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
