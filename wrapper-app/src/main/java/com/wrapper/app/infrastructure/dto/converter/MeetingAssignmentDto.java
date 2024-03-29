package com.wrapper.app.infrastructure.dto.converter;

import com.wrapper.app.domain.model.TimeGrain;

import lombok.Data;

@Data
public class MeetingAssignmentDto {

    private String id;
    private MeetingDto meeting;
    private ProstorijaDto prostorija;
    private TimeGrain startingTimeGrain;
    private boolean pinned;
}
