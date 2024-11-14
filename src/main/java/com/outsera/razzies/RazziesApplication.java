package com.outsera.razzies;

import com.outsera.razzies.movie.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RazziesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RazziesApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(MovieService movieService) {
		return args -> {
			// O caminho do arquivo está configurado no application.properties, na propriedade "csv.filepath"
			// está considerando o arquivo "movielist.csv" disponibilizado junto com as Especificações do Teste do processo de Recrutamento
			movieService.loadFromCsv();
		};
	}


}
