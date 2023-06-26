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
import java.util.UUID;

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

	public static void main(String[] args) {
		SpringApplication.run(WrapperAppApplication.class, args);
	}

	@Override
	public void run(String... args) {
//		CollectionNameProvider.setCollectionName("2022/23Z");
//		for(StudijskiProgram studijskiProgram : studijskiProgramRepository.findAll()) {
//			Optional<StudijskiProgramPredmeti> exists = studijskiProgramPredmetiRepository.findById(studijskiProgram.getId());
//			if(exists.isEmpty()) {
//				List<PredmetPredavac> predmetPredavac = new ArrayList<>();
//				StudijskiProgramPredmeti studijskiProgramPredmeti = new StudijskiProgramPredmeti();
//				studijskiProgramPredmeti.setId(studijskiProgram.getId());
//				studijskiProgramPredmeti.setPredmetPredavaci(predmetPredavac);
//				studijskiProgramPredmeti.setStudijskiProgram(studijskiProgram);
//				studijskiProgramPredmetiRepository.save(studijskiProgramPredmeti);
//			}
//		}
//
//		for(StudijskiProgramPredmeti studijskiProgramPredmeti : studijskiProgramPredmetiRepository.findAll()) {
//			for (PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
//				if (!predmetPredavac.getPredmet().getStudijskiProgram().getId().equals(studijskiProgramPredmeti.getStudijskiProgram().getId())) {
//					Optional<Predmet> predmet = predmetRepository.findByOznakaAndPlanAndStudijskiProgram(predmetPredavac.getPredmet().getOznaka(), predmetPredavac.getPredmet().getPlan(), studijskiProgramPredmeti.getStudijskiProgram().getId());
//					if (predmet.isEmpty()) {
//						System.out.println(predmetPredavac.getPredmet().getOznaka());
//						Predmet predmet1 = predmetPredavac.getPredmet();
//						predmet1.setId(UUID.randomUUID().toString());
//						predmet1.setStudijskiProgram(studijskiProgramPredmeti.getStudijskiProgram());
//					}
//					predmet.ifPresent(predmetPredavac::setPredmet);
//				}
//			}
//			studijskiProgramPredmeti.updateBlockStatus();
//			studijskiProgramPredmetiRepository.save(studijskiProgramPredmeti);
//		}
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
