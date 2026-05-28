package br.unisinos.edl.filas.core.estruturas;

import java.util.ArrayList;
import java.util.List;

public class Pilha<T> {
    private No<T> topo;
    private int tamanho;

    public Pilha() {
        this.topo = null;
        this.tamanho = 0;

    }

    public void empilhar(T elemento) {
        No<T> novoNo = new No<>(elemento);
        novoNo.setProximo(topo);
        topo = novoNo;
        tamanho++;
    }

    public T desempilhar() {
        if (estaVazia()) return null;
        T conteudo = topo.getDado();
        topo = topo.getProximo();
        tamanho--;
        return conteudo;
    }


    public T espiar() {
        if (estaVazia()) return null;
        return topo.getDado();
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public List<T> paraLista() {
        return paraLista(0);
    }

    public List<T> paraLista(int limite) {
        List<T> lista = new ArrayList<>();
        No<T> atual = topo;
        int contador = 0;

        while (atual != null && (limite == 0 || contador < limite)) {
            lista.add(atual.getDado());
            atual = atual.getProximo();
            contador++;
        }
        return lista;
    }
}
