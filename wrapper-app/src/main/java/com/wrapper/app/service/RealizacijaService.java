package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.*;
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

    public RealizacijaService(RealizacijaRepository repository, PredavacService predavacService,
                              StudijskiProgramService studijskiProgramService, PredmetService predmetService) {
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

    public Realizacija addPredmet(String studijskiProgramId, PredmetPredavac predmetPredavac) {
        boolean block = checkPredmet(predmetPredavac);
        predmetPredavac.setBlock(block);
        checkStudijskiProgram(studijskiProgramId, predmetPredavac);
        checkPredavaci(predmetPredavac);
        Realizacija realizacija = getAll().get(0);
        Realizacija updated = realizacija.addPredmet(studijskiProgramId, predmetPredavac);
        predmetService.updateStatus(predmetPredavac.getPredmetId(), true);
        return repository.save(updated);
    }

    private boolean checkPredmet(PredmetPredavac predmetPredavac) {
        Predmet predmet = predmetService.getById(predmetPredavac.getPredmetId());
        boolean checkAsistentZauzeca = predmetPredavac.getAsistentZauzeca().isEmpty() &&
                (predmet.getBrojCasovaAud() != 0 || predmet.getBrojCasovaLab() != 0
                        || predmet.getBrojCasovaRac() != 0);
        return predmetPredavac.getProfesorId() == null || checkAsistentZauzeca;
    }

    public Realizacija updatePredmet(String studProgramId, String predmetId, PredmetPredavac predmetPredavac) {
        boolean block = checkPredmet(predmetPredavac);
        predmetPredavac.setBlock(block);
        checkStudijskiProgram(studProgramId, predmetPredavac);
        checkPredavaci(predmetPredavac);
        Realizacija realizacija = getAll().get(0);
        Realizacija updated = realizacija.updatePredmet(studProgramId, predmetId, predmetPredavac);
        return repository.save(updated);
    }

    private void checkStudijskiProgram(String studProgramId, PredmetPredavac predmetPredavac) {
        boolean block = predmetPredavac.isBlock();
        if(!block) {
            Realizacija realizacija = repository.findStudijskiProgramPredmetiByStudijskiProgramId(studProgramId);
            StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().get(0);
            for(PredmetPredavac predmet : studijskiProgramPredmeti.getPredmetPredavaci()) {
                if(predmet.isBlock() && !predmet.getPredmetId().equals(predmetPredavac.getPredmetId())) {
                    block = true;
                    break;
                }
            }
        }
        studijskiProgramService.updateStatus(studProgramId, block);
    }

    private void checkPredavaci(PredmetPredavac predmetPredavac) {
        if(predmetPredavac.getProfesorId() != null) {
            predavacService.getById(predmetPredavac.getProfesorId());
        }
        predmetPredavac.getOstaliProfesori().forEach(predavacService::getById);
        predmetPredavac.getAsistentZauzeca().forEach(az -> predavacService.getById(az.getAsistentId()));
    }

    public StudijskiProgramPredmetiDto getStudijskiProgramById(String studijskiProgramId) {
        Realizacija realizacija = repository.findStudijskiProgramPredmetiByStudijskiProgramId(studijskiProgramId);
        StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().get(0);
        StudijskiProgramPredmetiDto studijskiProgramPredmetiDto = new StudijskiProgramPredmetiDto();
        fillStudijskiProgramInfo(studijskiProgramPredmeti.getStudijskiProgramId(), studijskiProgramPredmetiDto);
        for(PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
            PredmetPredavacDto predmetPredavacDto = new PredmetPredavacDto();
            fillPredmetInfo(predmetPredavac.getPredmetId(), predmetPredavacDto);
            fillProfesorInfo(predmetPredavac.getProfesorId(), predmetPredavacDto);
            fillOstaliProfesoriInfo(predmetPredavac.getOstaliProfesori(), predmetPredavacDto);
            fillAsistentiInfo(predmetPredavac.getAsistentZauzeca(), predmetPredavacDto);
            predmetPredavacDto.setBlock(predmetPredavac.isBlock());
            studijskiProgramPredmetiDto.getPredmetPredavaci().add(predmetPredavacDto);
        }
        return studijskiProgramPredmetiDto;
    }

    private void fillStudijskiProgramInfo(String studijskiProgramId, StudijskiProgramPredmetiDto studijskiProgramPredmetiDto) {
        StudijskiProgram studijskiProgram = studijskiProgramService.getById(studijskiProgramId);
        studijskiProgramPredmetiDto.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
    }

    private void fillPredmetInfo(String predmetId, PredmetPredavacDto predmetPredavacDto) {
        Predmet predmet = predmetService.getById(predmetId);
        predmetPredavacDto.setPredmetId(predmet.getId());
        predmetPredavacDto.setPredmetPlan(predmet.getPlan());
        predmetPredavacDto.setPredmetOznaka(predmet.getOznaka());
        predmetPredavacDto.setPredmetNaziv(predmet.getNaziv());
        predmetPredavacDto.setPredmetGodina(predmet.getGodina());
    }

    private void fillProfesorInfo(String profesorId, PredmetPredavacDto predmetPredavacDto) {
        if(profesorId != null && predavacService.existsById(profesorId)) {
            Predavac profesor = predavacService.getById(profesorId);
            String profesorNaziv = (profesor.getTitula() != null ? profesor.getTitula() : "") + " " + profesor.getIme() + " " + profesor.getPrezime();
            predmetPredavacDto.setProfesor(profesorNaziv.trim());
        } else {
            predmetPredavacDto.setBlock(true);
            System.out.println("Profesor: " + profesorId);
        }
    }

    private void fillOstaliProfesoriInfo(List<String> ostaliProfesori, PredmetPredavacDto predmetPredavacDto) {
        for(String profId : ostaliProfesori) {
            if(!predavacService.existsById(profId)) {
                System.out.println("Ostali profesor: " + profId);
                continue;
            }
            Predavac ostaliProfesor = predavacService.getById(profId);
            String profNaziv = (ostaliProfesor.getTitula() != null ? ostaliProfesor.getTitula() : "") + " " + ostaliProfesor.getIme() + " " + ostaliProfesor.getPrezime();
            predmetPredavacDto.getOstaliProfesori().add(profNaziv.trim());
        }
    }

    private void fillAsistentiInfo(List<AsistentZauzece> asistentiZauzeca, PredmetPredavacDto predmetPredavacDto) {
        for(AsistentZauzece zauzece : asistentiZauzeca) {
            if(!predavacService.existsById(zauzece.getAsistentId())) {
                System.out.println("Asistent: " + zauzece.getAsistentId());
                continue;
            }
            AsistentiZauzecaDto zauzeceDto = new AsistentiZauzecaDto();
            Predavac asistent = predavacService.getById(zauzece.getAsistentId());
            String asistentNaziv = (asistent.getTitula() != null ? asistent.getTitula() : "") + " " + asistent.getIme() + " " + asistent.getPrezime();
            zauzeceDto.setAsistent(asistentNaziv.trim());
            zauzeceDto.setBrojTermina(zauzece.getBrojTermina());
            predmetPredavacDto.getAsistentiZauzeca().add(zauzeceDto);
        }
        Predmet predmet = predmetService.getById(predmetPredavacDto.getPredmetId());
        if(predmetPredavacDto.getAsistentiZauzeca().isEmpty() &&
                (predmet.getBrojCasovaAud() != 0 || predmet.getBrojCasovaRac() != 0 || predmet.getBrojCasovaLab() != 0)){
            predmetPredavacDto.setBlock(true);
        }
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

    public void removePredmet(String predmetId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removePredmet(predmetId));
        predmetService.deleteById(predmetId);
    }

    public void removePredavac(String predavacId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removePredavac(predavacId));
        predavacService.deleteById(predavacId);
    }

    public void removeStudijskiProgram(String studProgramId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removeStudijskiProgram(studProgramId));
        studijskiProgramService.deleteById(studProgramId);
        predmetService.deleteAllByStudijskiProgram(studProgramId);
    }
}
