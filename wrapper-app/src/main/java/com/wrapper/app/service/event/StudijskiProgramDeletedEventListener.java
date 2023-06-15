package com.wrapper.app.service.event;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.service.PredmetService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudijskiProgramDeletedEventListener implements ApplicationListener<StudijskiProgramDeletedEvent> {

    private final PredmetService predmetService;

    public StudijskiProgramDeletedEventListener(PredmetService predmetService) {
        this.predmetService = predmetService;
    }

    @Override
    public void onApplicationEvent(StudijskiProgramDeletedEvent event) {
        String studijskiProgramId = event.getStudijskiProgramId();
        List<Predmet> predmeti = predmetService.getByStudijskiProgram(studijskiProgramId, false);
        for (Predmet predmet : predmeti) {
            predmetService.deleteById(predmet.getId());
        }
    }
}

