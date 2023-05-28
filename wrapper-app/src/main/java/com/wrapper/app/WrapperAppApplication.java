package com.wrapper.app;

import com.wrapper.app.domain.*;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.repository.PredmetRepository;
import com.wrapper.app.repository.RealizacijaRepository;
import com.wrapper.app.repository.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WrapperAppApplication implements CommandLineRunner {

	@Autowired
	private RealizacijaRepository realizacijaRepository;

	@Autowired
	private StudijskiProgramRepository studijskiProgramRepository;

	@Autowired
	private PredmetRepository predmetRepository;

	public static void main(String[] args) {
		SpringApplication.run(WrapperAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CollectionNameProvider.setCollectionName("2022/23Z");
		Realizacija realizacija = realizacijaRepository.findAll().get(0);
		for(StudijskiProgram studijskiProgram : studijskiProgramRepository.findAll()) {
			Realizacija exists = realizacijaRepository.findStudijskiProgramPredmetiByStudijskiProgramId(studijskiProgram.getId());
			if(exists == null) {
				List<PredmetPredavac> predmetPredavac = new ArrayList<>();
				StudijskiProgramPredmeti studijskiProgramPredmeti = new StudijskiProgramPredmeti();
				studijskiProgramPredmeti.setStudijskiProgramId(studijskiProgram.getId());
				studijskiProgramPredmeti.setPredmetPredavaci(predmetPredavac);
				realizacija.getStudijskiProgramPredmeti().add(studijskiProgramPredmeti);
			}
		}

		realizacijaRepository.save(realizacija);

		for(Predmet predmet : predmetRepository.findAll()) {
			boolean uRealizaciji = false;
			Realizacija realizacijaa = realizacijaRepository.findStudijskiProgramPredmetiByStudijskiProgramId(predmet.getStudijskiProgram().getId());
			StudijskiProgramPredmeti studijskiProgramPredmeti = realizacijaa.getStudijskiProgramPredmeti().get(0);
			for(PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
				if(predmetPredavac.getPredmetId().equals(predmet.getId())) {
					uRealizaciji = true;
					break;
				}
			}
			if(predmet.getId().equals("c560767b-cc62-46cf-aa8d-633e3a737f60")) {
				System.out.println("OK");
			}
			predmet.setURealizaciji(uRealizaciji);
			predmetRepository.save(predmet);
		}
		System.out.println("***************************** GOTOVO ***************************************");
	}
}
