package br.com.gabriel.Donflix;

import br.com.gabriel.Donflix.main.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DonflixApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(DonflixApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibirMenu();


	}
}
