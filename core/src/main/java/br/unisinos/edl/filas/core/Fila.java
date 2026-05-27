package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.No;

public class Fila<T> {

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


    public void enfileirar(T elemento) {
        No<T> novoNo = new No<>(elemento);
        if (estaVazia()) {
            inicio = novoNo;
        } else {
            fim.setProximo(novoNo);
        }
        fim = novoNo;
        tamanho++;
    }


    public T desenfileirar() {
        if (estaVazia()) {
            return null;
        }
        T conteudo = inicio.getDado();
        inicio = inicio.getProximo();
        tamanho--;
        if (inicio == null) {
            fim = null;
        }
        return conteudo;
    }


    public T espiar() {
        if (estaVazia()) return null;
        return inicio.getDado();
    }


    public boolean removerElemento(T elemento) {
        if (estaVazia()) return false;

        if (inicio.getDado().equals(elemento)) {
            desenfileirar();
            return true;
        }

        No<T> atual = inicio;
        while (atual.getProximo() != null) {
            if (atual.getProximo().getDado().equals(elemento)) {
                No<T> noParaRemover = atual.getProximo();
                atual.setProximo(noParaRemover.getProximo());
                if (noParaRemover == fim) {
                    fim = atual;
                }
                tamanho--;
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }
}