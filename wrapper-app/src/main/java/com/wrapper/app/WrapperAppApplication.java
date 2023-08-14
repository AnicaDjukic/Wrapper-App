package com.wrapper.app;

import com.wrapper.app.application.service.StudijskiProgramPredmetiService;
import com.wrapper.app.infrastructure.persistence.repository.PredmetRepository;
import com.wrapper.app.infrastructure.persistence.repository.StudijskiProgramPredmetiRepository;
import com.wrapper.app.infrastructure.persistence.repository.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
