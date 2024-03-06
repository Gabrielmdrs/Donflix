package br.com.gabriel.Donflix.Command;

import br.com.gabriel.Donflix.model.Episodio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListaMelhoresEpCommand implements Command {
    private List<Episodio> episodios;

    public ListaMelhoresEpCommand(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    @Override
    public void execute() {
        System.out.println("Esses são os 5 melhores episodios: ");
        List<Episodio> toSort = new ArrayList<>();
        for (Episodio episodio : episodios) {
            toSort.add(episodio);
        }
        toSort.sort(Comparator.comparing(Episodio::getAvaliacao).reversed());
        long limit = 5;
        for (Episodio episodio : toSort) {
            if (limit-- == 0) break;
            System.out.println(episodio);
        }

    }
}
