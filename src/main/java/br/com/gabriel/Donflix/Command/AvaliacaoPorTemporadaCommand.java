package br.com.gabriel.Donflix.Command;

import br.com.gabriel.Donflix.domain.Episodio;
import br.com.gabriel.Donflix.service.SerieService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvaliacaoPorTemporadaCommand implements Command {
    private List<Episodio> episodios;


    public AvaliacaoPorTemporadaCommand(List<Episodio> episodios){
        this.episodios = episodios;
    }

    @Override
    public void execute() {
        Map<Integer, Double> avaliacaoTemporada = this.episodios.stream()
                .filter(e -> e.getAvaliacao()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoTemporada);

    }




}
