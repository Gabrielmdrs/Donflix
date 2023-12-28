package br.com.gabriel.Donflix.main;

import br.com.gabriel.Donflix.Command.*;
import br.com.gabriel.Donflix.domain.DadosTemporada;
import br.com.gabriel.Donflix.domain.Episodio;
import br.com.gabriel.Donflix.service.ConsumoAPI;
import br.com.gabriel.Donflix.service.ConverteDados;
import br.com.gabriel.Donflix.service.SerieService;
import java.io.IOException;
import java.util.*;


public class Principal {

   private ConsumoAPI consumoAPI = new ConsumoAPI();
   private ConverteDados conversor = new ConverteDados();
   private final String ENDERECO = "https://www.omdbapi.com/?t=";
   private final String API_KEY = "&apikey=5295ad80";
   private List<DadosTemporada> temporadas = new ArrayList<>();

    public void exibirMenu() throws IOException, InterruptedException {
        SerieService serieService = new SerieService();
        CommandExecutor commandExecutor = new CommandExecutor();
        System.out.println("---- Boas Vindas ao Donflix -----");
        System.out.println("Qual serie gostaria de pesquisar?");
        try{
            List<Episodio> episodios = serieService.buscaSerie();

            int opcaoEscolhida = 0;
            while (opcaoEscolhida!=5){
                System.out.println("Qual opção gostaria?");
                System.out.println("1 - Listar todos os epsodios da serie");
                System.out.println("2 - Buscar episodio apartir de um ano");
                System.out.println("3 - Buscar episodio por titulo");
                System.out.println("4 - Obter a avalição das temporadas");
                System.out.println("5 - Sair");

                String textoDigitado = new Scanner(System.in).nextLine();
                opcaoEscolhida = Integer.parseInt(textoDigitado);
                switch (opcaoEscolhida){
                    case 1 -> commandExecutor.executeCommand(new ListaTodosEpisodiosCommand(episodios));
                    case 2 -> commandExecutor.executeCommand(new BuscaEpPorDataCommand(episodios));
                    case 3 -> commandExecutor.executeCommand(new BuscaEpPorTituloCommand(episodios));
                    case 4 -> commandExecutor.executeCommand(new AvaliacaoPorTemporadaCommand(episodios));
                    case 5 -> System.exit(0);
                    default -> opcaoEscolhida = 0;
                }

            }



        }catch (Exception e){
            System.out.println(e);
        }



    }



}
