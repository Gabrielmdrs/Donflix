package br.com.gabriel.Donflix.Command;

import br.com.gabriel.Donflix.model.Episodio;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BuscaEpPorTituloCommand implements Command{
    private Scanner input = new Scanner(System.in);
    List<Episodio> episodios;

    public BuscaEpPorTituloCommand(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    @Override
    public void execute() {
        System.out.println("Digite o trecho do titulo do episodio!");
        var trechoTitulo = input.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado!");
            System.out.println(episodioBuscado.get());
        }
    }
}
