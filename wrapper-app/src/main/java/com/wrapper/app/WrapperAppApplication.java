package com.wrapper.app;

import com.wrapper.app.domain.*;
import com.wrapper.app.repository.*;
import com.wrapper.app.service.StudijskiProgramPredmetiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class WrapperAppApplication implements CommandLineRunner {

	@Autowired
	private StudijskiProgramPredmetiService studijskiProgramPredmetiService;

	@Autowired
	private StudijskiProgramPredmetiRepository studijskiProgramPredmetiRepository;

	@Autowired
	private StudijskiProgramRepository studijskiProgramRepository;

	@Autowired
	private PredmetRepository predmetRepository;

	@Autowired
	private RealizacijaRepository realizacijaRepository;

	public static void main(String[] args) {
		SpringApplication.run(WrapperAppApplication.class, args);
	}

	@Override
	public void run(String... args) {
//		CollectionNameProvider.setCollectionName("2022/23Z");
//		Realizacija realizacija = realizacijaRepository.findAll().get(0);
//		for(StudijskiProgram studijskiProgram : studijskiProgramRepository.findAll()) {
//			Optional<StudijskiProgramPredmeti> exists = studijskiProgramPredmetiRepository.findById(studijskiProgram.getId());
//			if(exists.isEmpty()) {
//				List<PredmetPredavac> predmetPredavac = new ArrayList<>();
//				StudijskiProgramPredmeti studijskiProgramPredmeti = new StudijskiProgramPredmeti();
//				studijskiProgramPredmeti.setId(studijskiProgram.getId());
//				studijskiProgramPredmeti.setPredmetPredavaci(predmetPredavac);
//				studijskiProgramPredmeti.setStudijskiProgram(studijskiProgram);
//				studijskiProgramPredmetiRepository.save(studijskiProgramPredmeti);
//				realizacija.getStudijskiProgramPredmeti().add(studijskiProgramPredmeti);
//			}
//		}
//
//		for(StudijskiProgramPredmeti studijskiProgramPredmeti: realizacija.getStudijskiProgramPredmeti()) {
//			if(studijskiProgramPredmeti.getStudijskiProgram() == null) {
//				studijskiProgramPredmeti.setStudijskiProgram(studijskiProgramRepository.findById(studijskiProgramPredmeti.getId()).get());
//				studijskiProgramPredmetiRepository.save(studijskiProgramPredmeti);
//			}
//		}
//
//		realizacijaRepository.save(realizacija);
//
//		for(Predmet predmet : predmetRepository.findAll()) {
//			boolean uRealizaciji = false;
//			StudijskiProgramPredmeti studijskiProgramPredmeti = studijskiProgramPredmetiService.getById(predmet.getStudijskiProgram().getId());
//			for(PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
//				if(predmetPredavac.getPredmet().getId().equals(predmet.getId())) {
//					uRealizaciji = true;
//					break;
//				}
//			}
//			predmet.setURealizaciji(uRealizaciji);
//			predmetRepository.save(predmet);
//		}
		System.out.println("***************************** GOTOVO ***************************************");
	}
}
