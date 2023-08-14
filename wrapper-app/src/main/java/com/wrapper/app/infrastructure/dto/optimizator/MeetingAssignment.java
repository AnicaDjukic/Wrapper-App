package com.wrapper.app.infrastructure.dto.optimizator;

import com.wrapper.app.domain.model.Prostorija;
import com.wrapper.app.domain.model.TimeGrain;
import lombok.Data;

import java.util.UUID;

@Data
public class MeetingAssignment {

    private UUID id;
    private MeetingDto meeting;
    private Prostorija prostorija;
    private TimeGrain startingTimeGrain;
    private boolean pinned;
}
