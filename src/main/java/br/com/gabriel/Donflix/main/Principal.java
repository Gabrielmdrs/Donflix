package br.com.gabriel.Donflix.main;

import br.com.gabriel.Donflix.Command.*;
import br.com.gabriel.Donflix.model.Episodio;
import br.com.gabriel.Donflix.repository.SerieRepository;
import br.com.gabriel.Donflix.service.SerieService;
import java.io.IOException;
import java.util.*;


public class Principal {
    private Scanner input = new Scanner(System.in);
    private List<Episodio> episodios = new ArrayList<>();
    private SerieRepository repositorio;


   public Principal(SerieRepository repositorio){
       this.repositorio = repositorio;
   }

    public void exibirMenu() throws IOException, InterruptedException {
        SerieService serieService = new SerieService(getRepositorio());
        CommandExecutor commandExecutor = new CommandExecutor();
        System.out.println("---- Boas Vindas ao Donflix -----");
        System.out.println("\n____________Menu________________\n");

        try{
            var opcaoEscolhida = -1;

            while (opcaoEscolhida != 0) {
                System.out.println("1 - Pesquisar serie");
                System.out.println("2 - Listar series buscadas");
                System.out.println("0 - Sair");

                opcaoEscolhida = input.nextInt();
                if (opcaoEscolhida == 1){
                    serieService.buscaSerie();


                    while (opcaoEscolhida != 5) {
                        System.out.println("Qual opção gostaria?");
                        System.out.println("1 - Listar todos os episodios da serie");
                        System.out.println("2 - Buscar episodio apartir de um ano");
                        System.out.println("3 - Buscar episodio por titulo");
                        System.out.println("4 - Obter a avalição das temporadas");
                        System.out.println("5 - Voltar");

                        opcaoEscolhida = input.nextInt();
                        switch (opcaoEscolhida) {
                            case 1 -> commandExecutor.executeCommand(new ListaTodosEpisodiosCommand(episodios));
                            case 2 -> commandExecutor.executeCommand(new BuscaEpPorDataCommand(episodios));
                            case 3 -> commandExecutor.executeCommand(new BuscaEpPorTituloCommand(episodios));
                            case 4 -> commandExecutor.executeCommand(new AvaliacaoPorTemporadaCommand(episodios));
                            case 5 -> opcaoEscolhida = 5;
                            default -> System.out.println("Opção invalida");

                        }
                    }

                } else if (opcaoEscolhida == 2) {
                    serieService.ListarSeries();

                    while (opcaoEscolhida != 0){
                        System.out.println("Qual opção gostaria?");
                        System.out.println("1 - Buscar serie por titulo");
                        System.out.println("2 - Buscar series por ator");
                        System.out.println("3 - Buscar as top 5 Series");
                        System.out.println("4- Buscar series para maratonar");
                        System.out.println("5 - Buscar episodio por trecho");
                        System.out.println("6 - Top 5 episodios por serie");
                        System.out.println("0- Sair");

                        opcaoEscolhida = input.nextInt();
                        switch (opcaoEscolhida){
                            case 0 -> opcaoEscolhida = 0;
                            case 1 -> serieService.buscarSeriePorTitulo();
                            case 2 -> serieService.buscarSeriePorAtor();
                            case 3 -> serieService.buscaTop5Series();
                            case 4 -> serieService.filtrarSeriesPorTemporadaEAvaliacao();
                            case 5 -> serieService.buscarEpisodioPorTrecho();
                            case 6 -> serieService.top5EpisodiosPorSerie();

                        }
                    }

                } else {
                    System.out.println("Saindo....");
                    opcaoEscolhida = 0;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public SerieRepository getRepositorio() {
        return repositorio;
    }


}
