package br.unisinos.edl.filas.core;

import java.util.function.Predicate;

public class Fila<T> {
<<<<<<< Updated upstream
=======
    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public No<T> getInicio() {
        this.inicio = inicio;
        return inicio;
    }
>>>>>>> Stashed changes

    public void enfileirar(T elemento) {
        // TODO: Implementar
    }

    public T desenfileirar() {
        return null; // TODO: Implementar
    }

    public T espiar() {
        return null; // TODO: Implementar
    }

    public boolean isVazio() {
        return true; // TODO: Implementar
    }

    public void remover(int indice) {
        // TODO: Implementar
    }

    public void removerQuando(Predicate<T> filtro) {
       // TODO: Implementar
    }
}
