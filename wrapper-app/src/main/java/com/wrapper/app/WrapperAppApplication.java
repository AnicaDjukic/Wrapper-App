package com.wrapper.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wrapper.app.config.MongoDBConfig;
import com.wrapper.app.domain.Predmet;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.repository.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MongoDBConfig.class})
public class WrapperAppApplication implements CommandLineRunner {

	@Autowired
	private PredmetRepository predmetRepository;

	@Override
	public void run(String... args) throws Exception {
//		//collectionNameResolver.setCollectionName("");
//		CollectionNameProvider.setCollectionName("");
//		for(Predmet predmet : predmetRepository.findAll()) {
//			System.out.println(predmet);
//		}
//		System.out.println("------------------------------------------------------------");
//
//		CollectionNameProvider.setCollectionName("2023");
//		//collectionNameResolver.setCollectionName("2023");
//		for(Predmet predmet : predmetRepository.findAll()) {
//			System.out.println(predmet);
//		}
//
//		//Predmet insert = predmetRepository.findAll().get(0);
//
////		// Connect to MongoDB server
////		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
////
////		// Access a specific database
////		MongoDatabase database = mongoClient.getDatabase("WrapperAppDatabase");
////
////		// Access a specific collection
////		database.createCollection("Predmeti2024");
////		MongoCollection<Predmet> collection = database.getCollection("Predmeti2024").withDocumentClass(Predmet.class);
////
////		// Create MappingMongoConverter with custom conversions
////
////		collection.insertOne(insert);
////
////		// Close the MongoDB client
////		mongoClient.close();
//
//		CollectionNameProvider.setCollectionName("");
		CollectionNameProvider.setCollectionName("");
	}

	public static void main(String[] args) {
		SpringApplication.run(WrapperAppApplication.class, args);
	}
}
