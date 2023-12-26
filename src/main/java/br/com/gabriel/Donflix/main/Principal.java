package br.com.gabriel.Donflix.main;

import br.com.gabriel.Donflix.domain.DadosEpisodio;
import br.com.gabriel.Donflix.domain.DadosSerie;
import br.com.gabriel.Donflix.domain.DadosTemporada;
import br.com.gabriel.Donflix.domain.Episodio;
import br.com.gabriel.Donflix.service.ConsumoAPI;
import br.com.gabriel.Donflix.service.ConverteDados;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
   private Scanner input = new Scanner(System.in);
   private ConsumoAPI consumoAPI = new ConsumoAPI();
   private ConverteDados conversor = new ConverteDados();
   private final String ENDERECO = "https://www.omdbapi.com/?t=";
   private final String API_KEY = "&apikey=5295ad80";
   private List<DadosTemporada> temporadas = new ArrayList<>();

    public void exibirMenu() throws IOException, InterruptedException {

    //listando as temporadas da seriedesejada
        System.out.println("Qual serie gostaria de pesquisar?");
        String serieDesejada = input.nextLine().replaceAll(" ","+");

        String json = consumoAPI.ObterDados(ENDERECO + serieDesejada + API_KEY);


        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);


        for (int i = 1; i<=dadosSerie.totalTemporadas();i++) {
            json = consumoAPI.ObterDados(ENDERECO + serieDesejada + "&season=" + i + API_KEY);
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);



        }
//        temporadas.forEach(System.out::println);

//        List<DadosEpisodio> DadoseEpisodios = temporadas.stream()
//                                        .flatMap(t -> t.episodios().stream())
//                                        .collect(Collectors.toList());
//        System.out.println("Melhores epsodios "+ serieDesejada);
//        DadoseEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        //LISTANDO OS EPISODIOS DAS TEMPORADAS E MOSTRANDO O TOP 5
        List<Episodio> episodios = temporadas.stream()
                                .flatMap(t -> t.episodios().stream()
                                                .map(d -> new Episodio(t.numero(),d))
                                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
        Map<Integer, Double> avaliacaoTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Media de avaliação: " + est.getAverage());

//        System.out.println("Esses são os 5 melhores episodios da série " + serieDesejada);
//        episodios.stream()
//                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);
//
//        //Buscando episodio apartir do trecho do titulo
//        System.out.println("Digite o trecho do titulo do episodio!");
//        var trechoTitulo = input.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().contains(trechoTitulo))
//                .findFirst();
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado!");
//            System.out.println(episodioBuscado.get());
//        }
//
//        // BUSCANDO EPISODIOS APARTIR DE UMA DATA
//        System.out.println("Apartir de qual data gostaria de buscar os episodios?");
//        var ano = input.nextInt();
//        input.nextLine();
//
//        LocalDate dataDesejada = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento()!= null && e.getDataLancamento().isAfter(dataDesejada))
//                .forEach(e -> System.out.println(
//                        "Temporadas: " + e.getTemporada() +
//                                " Episodio: " + e.getTitulo() +
//                                " Data Lançamento: " + e.getDataLancamento().format(formatador)
//                ));



    }



}
