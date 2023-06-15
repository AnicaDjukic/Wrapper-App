package com.wrapper.app.service.event;

import org.springframework.context.ApplicationEvent;

public class StudijskiProgramDeletedEvent extends ApplicationEvent {

    private final String studijskiProgramId;

    public StudijskiProgramDeletedEvent(String studijskiProgramId) {
        super(studijskiProgramId);
        this.studijskiProgramId = studijskiProgramId;
    }

    public String getStudijskiProgramId() {
        return studijskiProgramId;
    }
}

