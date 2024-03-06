package br.com.gabriel.Donflix.DTO;

import br.com.gabriel.Donflix.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO( long id,
                        String titulo,
                        Integer totalTemporadas,
                        Double avaliacao,
                        Categoria genero,
                        String atores,
                        String poster,
                        String sinopse) {
}
