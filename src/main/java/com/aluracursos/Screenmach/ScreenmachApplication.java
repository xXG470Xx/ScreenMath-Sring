package com.aluracursos.Screenmach;

import com.aluracursos.Screenmach.model.DatosEpisodio;
import com.aluracursos.Screenmach.model.DatosSerie;
import com.aluracursos.Screenmach.model.DatosTemporadas;
import com.aluracursos.Screenmach.principal.Principal;
import com.aluracursos.Screenmach.service.ConsumoAPI;
import com.aluracursos.Screenmach.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmachApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraElMenu();
	}
}
