package br.com.gabriel.Donflix.Command;

import br.com.gabriel.Donflix.domain.Episodio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuscaEpPorDataCommand implements Command {
    private Scanner input = new Scanner(System.in);
    List<Episodio> episodios;

    public BuscaEpPorDataCommand(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    @Override
    public void execute() {
        System.out.println("Apartir de qual data gostaria de buscar os episodios?");
        var ano = input.nextInt();
        input.nextLine();

        LocalDate dataDesejada = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento()!= null && e.getDataLancamento().isAfter(dataDesejada))
                .forEach(e -> System.out.println(
                        "Temporadas: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));

    }
}
