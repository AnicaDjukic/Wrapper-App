package com.wrapper.app.application.service;

import com.wrapper.app.infrastructure.dto.converter.RasporedPrikaz;
import com.wrapper.app.infrastructure.dto.optimizator.Semestar;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.domain.model.*;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameProvider;
import com.wrapper.app.infrastructure.persistence.util.CollectionTypes;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingScheduleService {

    private final MongoTemplate mongoTemplate;

    public MeetingScheduleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MeetingSchedule createMeetingShedule(Database database, List<Meeting> meetings, List<MeetingAssignment> meetingAssignments) {
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        return new MeetingSchedule(
                getDepartmani(database),
                getKatedre(database),
                getStudijskiProgrami(database),
                getPredmeti(database),
                meetings,
                getDani(),
                getTimeGrains(),
                getProstorije(database),
                getPredavaci(database),
                getStudentskeGrupe(database),
                meetingAssignments,
                Semestar.valueOf(database.getSemestar().substring(0, 1))
        );
    }

    private List<OrganizacionaJedinica> getDepartmani(Database database) {
        String collectionName = CollectionTypes.ORGANIZACIONE_JEDINICE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(OrganizacionaJedinica.class, collectionName).stream().filter(o -> o.getDepartman() == null).toList();
    }

    private List<OrganizacionaJedinica> getKatedre(Database database) {
        String collectionName = CollectionTypes.ORGANIZACIONE_JEDINICE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(OrganizacionaJedinica.class, collectionName).stream().filter(o -> o.getDepartman() != null).toList();
    }

    private List<StudijskiProgram> getStudijskiProgrami(Database database) {
        String collectionName = CollectionTypes.STUDIJSKI_PROGRAMI + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(StudijskiProgram.class, collectionName);
    }

    private List<Predmet> getPredmeti(Database database) {
        String collectionName = CollectionTypes.PREDMETI + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Predmet.class, collectionName);
    }

    private List<Dan> getDani() {
        return mongoTemplate.findAll(Dan.class, Dan.class.getSimpleName());
    }

    private List<TimeGrain> getTimeGrains() {
        return mongoTemplate.findAll(TimeGrain.class, TimeGrain.class.getSimpleName());
    }

    private List<Prostorija> getProstorije(Database database) {
        String collectionName = CollectionTypes.PROSTORIJE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Prostorija.class, collectionName);
    }

    private List<Predavac> getPredavaci(Database database) {
        String collectionName = CollectionTypes.PREDAVACI + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Predavac.class, collectionName);
    }

    private List<StudentskaGrupa> getStudentskeGrupe(Database database) {
        String collectionName = CollectionTypes.STUDENTSKE_GRUPE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(StudentskaGrupa.class, collectionName);
    }

    public List<RasporedPrikaz> getRasporedPrikaz() {
        return mongoTemplate.findAll(RasporedPrikaz.class, CollectionTypes.RASPORED_PRIKAZ);
    }
}
