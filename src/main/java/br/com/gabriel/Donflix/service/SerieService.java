package br.com.gabriel.Donflix.service;

import br.com.gabriel.Donflix.DTO.EpisodioDTO;
import br.com.gabriel.Donflix.DTO.SerieDTO;
import br.com.gabriel.Donflix.model.*;
import br.com.gabriel.Donflix.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
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
    @Autowired
    private SerieRepository repository;

    public SerieService(SerieRepository repositorio){
        this.repository = repositorio;

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
        repository.save(serie);

    }

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repository.findAll());
    }
    public void ListarSeries() {
        this.listaSeries = repository.findAll();
        listaSeries.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    public void buscarSeriePorTitulo(){
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = input.nextLine();
        serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()) {
            System.out.println("Dados da serie: " + serieBuscada.get());
        }else {
            System.out.println("Serie não encontrada");
        }
    }
    public void buscarSeriePorAtor(){
        System.out.println("Qual ator gostaria de buscar?");
        var nomeAtor = input.nextLine();
        List<Serie> seriesEncontrada = repository.findByAtoresContainingIgnoreCase(nomeAtor);
        if(seriesEncontrada.size()>0) {
            System.out.println("Essas são as series que " + nomeAtor + " trabalhou: ");
            seriesEncontrada.forEach(s -> System.out.println("Serie: " + s.getTitulo()
                    + " \nAvaliação: " + s.getAvaliacao()));
        }else {
            System.out.println("Series não encontradas!");
        }

    }
    public List<SerieDTO> buscaTop5Series(){
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());

    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(),serie.getTitulo(),serie.getTotalTemporadas(),serie.getAvaliacao(),serie.getGenero(),serie.getAtores(),serie.getPoster(),serie.getSinopse()))
                .collect(Collectors.toList());
    }

    public void buscaSeriePorCategoria(){
        System.out.println("Qual categoria gostaria de buscar suas series?");
        var nomeGenero = input.nextLine();
        Categoria categoria = Categoria.fromProtugues(nomeGenero);
        List<Serie> seriesEncontradas = repository.findByGenero(categoria);
    }

    public void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Até quantas temporadas gostaria que tenha a serie?");
        Integer totalTemporadas = input.nextInt();
        Double avaliacao = 8.0;
        List<Serie> seriesParaMaratonar = repository.filtraPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Essas são as series de até " + totalTemporadas + " temporadas para maratonar: \n");
        seriesParaMaratonar.forEach(s -> System.out.println("Serie: "+ s.getTitulo() + ", Temporadas: "+ s.getTotalTemporadas()));
    }


    public void buscarEpisodioPorTrecho() {
        System.out.println("Qual trecho do episodio deseja buscar?");
        var trechoEpisodio = input.nextLine();
        List<Episodio> episodiosEncontrados = repository.buscaEpisodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(episodio ->
                System.out.printf("Serie: %s Temporada %s - Episodio: %s - %s \n",
                        episodio.getSerie().getTitulo(), episodio.getTemporada(),
                        episodio.getNumeroEpisodio(), episodio.getTitulo()));
    }

    public void top5EpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.top5Episodios(serie);
            topEpisodios.forEach(episodio ->
                    System.out.printf("Serie: %s Temporada %s - Episodio: %s - %s - Avaliação: %s \n",
                            episodio.getSerie().getTitulo(), episodio.getTemporada(),
                            episodio.getNumeroEpisodio(), episodio.getTitulo(), episodio.getAvaliacao()));

        }

    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            return new SerieDTO(serie.getId(),serie.getTitulo(),serie.getTotalTemporadas(),serie.getAvaliacao(),serie.getGenero(),serie.getAtores(),serie.getPoster(),serie.getSinopse());
        }
    return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            return serie.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()))
                    .collect(Collectors.toList());

        }
        return null;
    }

    public List<EpisodioDTO> obterEpisodiosPorTemporada(Long id, Long temporada) {
        List<Episodio> serieBuscada = repository.obterEpisodiosPorTemporada(id, temporada);
        return serieBuscada.stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),e.getNumeroEpisodio(),e.getTitulo()))
                .collect(Collectors.toList());

    }
}
