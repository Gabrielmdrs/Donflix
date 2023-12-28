package br.com.gabriel.Donflix.Command;

import br.com.gabriel.Donflix.domain.Episodio;

import java.util.List;

public class ListaTodosEpisodiosCommand implements Command {
    private List<Episodio> episodios;

    public ListaTodosEpisodiosCommand(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    @Override
    public void execute() {
        episodios.forEach(System.out::println);
    }
}
