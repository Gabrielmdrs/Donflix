package br.com.gabriel.Donflix.controller;

import br.com.gabriel.Donflix.DTO.EpisodioDTO;
import br.com.gabriel.Donflix.DTO.SerieDTO;
import br.com.gabriel.Donflix.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return service.obterTodasAsSeries();
    }
    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return service.buscaTop5Series();
    }
    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return service.obterLancamentos();
    }
    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return service.obterPorId(id);
    }
    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return service.obterTodasTemporadas(id);
    }
    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obterEpisodiosPorTemporada(@PathVariable Long id, @PathVariable Long temporada){
       return service.obterEpisodiosPorTemporada(id,temporada);
    }

}
