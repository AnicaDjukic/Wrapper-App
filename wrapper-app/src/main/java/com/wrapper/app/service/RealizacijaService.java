package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.AsistentiZauzecaDto;
import com.wrapper.app.dto.PredmetPredavacDto;
import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.dto.StudijskiProgramPredmetiDto;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.RealizacijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealizacijaService {

    private final RealizacijaRepository repository;

    private final PredavacService predavacService;

    private final StudijskiProgramService studijskiProgramService;

    private final PredmetService predmetService;

    public RealizacijaService(RealizacijaRepository repository, PredavacService predavacService, StudijskiProgramService studijskiProgramService, PredmetService predmetService) {
        this.repository = repository;
        this.predavacService = predavacService;
        this.studijskiProgramService = studijskiProgramService;
        this.predmetService = predmetService;
    }

    public List<Realizacija> getAll() {
        return repository.findAll();
    }

    public Realizacija getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Realizacija.class.getSimpleName()));
    }

    // rukovanje predavacima na predmetima
    public Realizacija addPredmet(String studijskiProgramId, RealizacijaRequestDto dto) {
        predmetService.getById(dto.getPredmetId()); // check if predmet exists
        Realizacija realizacija = getAll().get(0);
        checkPredavaci(dto);    // provera da li predavaci postoje
        // TODO: provera da li ukupan broj termina vezbi odgovara broju termina vezbi na predmetu
        Realizacija updated = realizacija.addPredmet(studijskiProgramId, dto);
        return repository.save(updated);
    }

    // provera da li predavaci postoje
    private void checkPredavaci(RealizacijaRequestDto dto) {
        predavacService.getById(dto.getProfesorId());
        dto.getOstaliProfesori().forEach(predavacService::getById);
        dto.getAsistentZauzeca().forEach(az -> predavacService.getById(az.getAsistentId()));
    }

    // TODO: refaktorisati
    public StudijskiProgramPredmetiDto getStudijskiProgramById(String studijskiProgramId) {
        Realizacija realizacija = repository.findStudijskiProgramPredmetiByStudijskiProgramId(studijskiProgramId);
        StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().get(0);
        StudijskiProgramPredmetiDto studijskiProgramPredmetiDto = new StudijskiProgramPredmetiDto();
        StudijskiProgram studijskiProgram = studijskiProgramService.getById(studijskiProgramPredmeti.getStudijskiProgramId());
        studijskiProgramPredmetiDto.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
        for(PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
            PredmetPredavacDto predmetPredavacDto = new PredmetPredavacDto();
            Predmet predmet = predmetService.getById(predmetPredavac.getPredmetId());
            predmetPredavacDto.setPredmetId(predmet.getId());
            predmetPredavacDto.setPredmetOznaka(predmet.getOznaka());
            predmetPredavacDto.setPredmetNaziv(predmet.getNaziv());
            predmetPredavacDto.setPredmetGodina(predmet.getGodina());
            Predavac profesor = predavacService.getById(predmetPredavac.getProfesorId());
            String profesorNaziv = profesor.getTitula() + " " + profesor.getIme() + " " + profesor.getPrezime();
            predmetPredavacDto.setProfesor(profesorNaziv.trim()); // TODO: dodati org jed
            for(String ostaliProfId : predmetPredavac.getOstaliProfesori()) {
                Predavac ostaliProfesor = predavacService.getById(ostaliProfId);
                String ostaliProfNaziv = ostaliProfesor.getTitula() + " " + ostaliProfesor.getIme() + " " + ostaliProfesor.getPrezime();
                predmetPredavacDto.getOstaliProfesori().add(ostaliProfNaziv.trim()); // TODO: dodati org jed
            }
            for(AsistentZauzece zauzece : predmetPredavac.getAsistentZauzeca()) {
                AsistentiZauzecaDto zauzeceDto = new AsistentiZauzecaDto();
                Predavac asistent = predavacService.getById(zauzece.getAsistentId());
                String asistentNaziv = asistent.getTitula() + " " + asistent.getIme() + " " + asistent.getPrezime();
                zauzeceDto.setAsistent(asistentNaziv.trim());
                zauzeceDto.setBrojTermina(zauzece.getBrojTermina());
                predmetPredavacDto.getAsistentiZauzeca().add(zauzeceDto);
            }
            studijskiProgramPredmetiDto.getPredmetPredavaci().add(predmetPredavacDto);
        }
        return studijskiProgramPredmetiDto;
    }

    public void deletePredmetInStudijskiProgram(String studijskiProgramId, String predmetId) {
        Realizacija realizacija = repository.findAll().get(0);
        StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().stream()
                .filter(p -> p.getStudijskiProgramId().equals(studijskiProgramId))
                .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        PredmetPredavac predmetPredavac = studijskiProgramPredmeti.getPredmetPredavaci().stream()
                        .filter(p -> p.getPredmetId().equals(predmetId))
                        .findFirst().orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
        studijskiProgramPredmeti.getPredmetPredavaci().remove(predmetPredavac);
        repository.save(realizacija);
    }
}
