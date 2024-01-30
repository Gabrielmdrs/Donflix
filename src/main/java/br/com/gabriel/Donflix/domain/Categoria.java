package br.com.gabriel.Donflix.domain;

public enum Categoria {
    ACAO("Action", "Ação"),
    COMEDIA("Comedy", "Comédia"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");
    private String categoriaPortugues;
    private String categoriaOmdb;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }
    public static Categoria fromOmdb(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromProtugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
