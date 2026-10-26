package dominio;

public class Participante {
    private final String nome;

    public Participante(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do participante não pode ser vazio.");
        }

        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }
}
