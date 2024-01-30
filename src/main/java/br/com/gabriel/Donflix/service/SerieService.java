package br.com.gabriel.Donflix.service;

import br.com.gabriel.Donflix.domain.*;
import br.com.gabriel.Donflix.repository.SerieRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SerieService {
    private Scanner input = new Scanner(System.in);
    private Optional<Serie> serieBuscada;
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5295ad80";
    private ConverteDados conversor = new ConverteDados();
    private List<DadosTemporada> temporadas = new ArrayList<>();
    private List<Serie> listaSeries = new ArrayList<>();
    private List<Episodio> episodios = new ArrayList<>();
    private String serieDesejada;
    private SerieRepository repositorio;

    public SerieService(SerieRepository repositorio){
        this.repositorio = repositorio;

    }

    public void buscaSerie() throws IOException, InterruptedException {
        System.out.println("Qual serie gostaria de pesquisar?");
        serieDesejada = input.nextLine().replaceAll(" ", "+");
        String json = consumoAPI.ObterDados(ENDERECO + serieDesejada + API_KEY);
        System.out.println(json);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        for (int i = 1; i<=dadosSerie.totalTemporadas(); i++) {
            try {
                json = consumoAPI.ObterDados(ENDERECO + serieDesejada + "&season=" + i + API_KEY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }

        episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ).collect(Collectors.toList());

        Serie serie = new Serie(dadosSerie);
        System.out.println(serie);
        this.episodios = episodios;
        serie.setEpisodios(episodios);
        repositorio.save(serie);

    }

    public void ListarSeries() {
        this.listaSeries = repositorio.findAll();
        listaSeries.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    public List<Episodio> listarEpisodios() {
        episodios.forEach(System.out::println);
        return episodios;
    }

    public void buscarSeriePorTitulo(){
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = input.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()) {
            System.out.println("Dados da serie: " + serieBuscada.get());
        }else {
            System.out.println("Serie não encontrada");
        }
    }
    public void buscarSeriePorAtor(){
        System.out.println("Qual ator gostaria de buscar?");
        var nomeAtor = input.nextLine();
        List<Serie> seriesEncontrada = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        if(seriesEncontrada.size()>0) {
            System.out.println("Essas são as series que " + nomeAtor + " trabalhou: ");
            seriesEncontrada.forEach(s -> System.out.println("Serie: " + s.getTitulo()
                    + " \nAvaliação: " + s.getAvaliacao()));
        }else {
            System.out.println("Series não encontradas!");
        }

    }
    public void buscaTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(s-> System.out.println("Serie: "+ s.getTitulo() + ", Avaliação: "+ s.getAvaliacao()));
    }

    public void buscaSeriePorCategoria(){
        System.out.println("Qual categoria gostaria de buscar suas series?");
        var nomeGenero = input.nextLine();
        Categoria categoria = Categoria.fromProtugues(nomeGenero);
        List<Serie> seriesEncontradas = repositorio.findByGenero(categoria);
    }

    public void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Até quantas temporadas gostaria que tenha a serie?");
        Integer totalTemporadas = input.nextInt();
        Double avaliacao = 8.0;
        List<Serie> seriesParaMaratonar = repositorio.filtraPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Essas são as series de até " + totalTemporadas + " temporadas para maratonar: \n");
        seriesParaMaratonar.forEach(s -> System.out.println("Serie: "+ s.getTitulo() + ", Temporadas: "+ s.getTotalTemporadas()));
    }


    public void buscarEpisodioPorTrecho() {
        System.out.println("Qual trecho do episodio deseja buscar?");
        var trechoEpisodio = input.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.buscaEpisodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(episodio ->
                System.out.printf("Serie: %s Temporada %s - Episodio: %s - %s \n",
                        episodio.getSerie().getTitulo(), episodio.getTemporada(),
                        episodio.getNumeroEpisodio(), episodio.getTitulo()));
    }

    public void top5EpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(episodio ->
                    System.out.printf("Serie: %s Temporada %s - Episodio: %s - %s - Avaliação: %s \n",
                            episodio.getSerie().getTitulo(), episodio.getTemporada(),
                            episodio.getNumeroEpisodio(), episodio.getTitulo(), episodio.getAvaliacao()));

        }

    }
}
