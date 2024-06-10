package com.aluracursos.Screenmach.principal;

import com.aluracursos.Screenmach.model.DatosEpisodio;
import com.aluracursos.Screenmach.model.DatosSerie;
import com.aluracursos.Screenmach.model.DatosTemporadas;
import com.aluracursos.Screenmach.model.Episodio;
import com.aluracursos.Screenmach.service.ConsumoAPI;
import com.aluracursos.Screenmach.service.ConvierteDatos;
import org.yaml.snakeyaml.events.CollectionEndEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";//El final significa darle valor de constante
    private final String API_KEY = "&apikey=989768bc";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.println("Escribe el nombre de la serie a buscar: ");
        //Busca los datos generales de las series
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE+ nombreSerie.replace(" ","+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas la temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas() ; i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json,DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas
//        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir toda la info a una lista del tipo DatosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 Episodios
//        System.out.println("Top 5 Episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro N/A"+e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo filtro mayor a menor"+e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer filtro MAyusculas"+e))
//                .limit(5)
//
//                .forEach(System.out::println);



        // Convirtiendo los datos a una lista del tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                        .collect(Collectors.toList());
       // episodios.forEach(System.out::println);

//        //Busqueda de episodios a partir de X año
//        System.out.println("indica el año a partir del cual deseas ver:");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();
//        LocalDate fechaBusqueda = LocalDate.of(fecha,1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy"); // Cambia el formato de la fecha
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                "Episodio" + e.getTitulo()+
//                                "Fecha de Lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
//                ));

//        //Busca Episodios por un pedazo del titulo
//        System.out.println("Esbribe el titulo del eqpisodio que desea ver:");
//        var pedazoTitulo = teclado.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episodio Encontrado");
//            System.out.println("Datos: "+ episodioBuscado.get());
//        }else {
//            System.out.println("Episodio no encontrado");
//        }


        Map <Integer,Double> evaluacionesPorTemporadas = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporadas);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() >0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de las evaluaciones: "+est.getAverage());
        System.out.println("Episodio mejor evaluado:"+ est.getMax());
        System.out.println("Episodio peor evaluado"+est.getMin());
    }
}
