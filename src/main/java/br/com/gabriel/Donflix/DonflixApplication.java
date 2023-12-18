package br.com.gabriel.Donflix;

import br.com.gabriel.Donflix.domain.DadosSerie;
import br.com.gabriel.Donflix.service.ConsumoAPI;
import br.com.gabriel.Donflix.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class DonflixApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(DonflixApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Scanner input = new Scanner(System.in);

		//String busca = input.nextLine().replaceAll(" ","&");
		ConsumoAPI consumoAPI = new ConsumoAPI();

		var json = consumoAPI.ObterDados("https://www.omdbapi.com/?t=the+office&apikey=5295ad80");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie novaSerie = conversor.obterDados(json, DadosSerie.class);
		System.out.println(novaSerie);

	}
}
