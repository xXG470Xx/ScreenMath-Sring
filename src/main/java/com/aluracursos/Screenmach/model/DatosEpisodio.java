package com.aluracursos.Screenmach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //ignorar todas las demas propiedades no mencionadas aqui
public record DatosEpisodio(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode")Integer numeroEpisodio,
        @JsonAlias("imdbRating")String evaluacion,
        @JsonAlias("Released")String fechaDeLanzamineto
) {

}
