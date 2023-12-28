package br.com.gabriel.Donflix.service;

import br.com.gabriel.Donflix.domain.DadosSerie;
import br.com.gabriel.Donflix.domain.DadosTemporada;
import br.com.gabriel.Donflix.domain.Episodio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SerieService {
    private Scanner input = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5295ad80";
    private ConverteDados conversor = new ConverteDados();
    private List<DadosTemporada> temporadas = new ArrayList<>();

    public List<Episodio> buscaSerie() throws IOException, InterruptedException {

        String serieDesejada = input.nextLine().replaceAll(" ","+");
        String json = consumoAPI.ObterDados(ENDERECO + serieDesejada + API_KEY);


        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);



        for (int i = 1; i<=dadosSerie.totalTemporadas();i++) {
            json = consumoAPI.ObterDados(ENDERECO + serieDesejada + "&season=" + i + API_KEY);
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ).collect(Collectors.toList());
        return episodios;

    }
}
