package br.unisinos.edl.filas.core;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.edl.filas.core.dominio.Senha;

public class FilaPrioritaria {
    private static FilaPrioritaria singleton;
    private int proximoNumero = 1;
    private final Fila<Senha> filaNormal;
    private final Fila<Senha> filaPrioridade;

public class FilaPrioritaria<T> {
    private static FilaPrioritaria<Senha> instancia; //Criar singleton
    private Fila<T> filaNormal;
    private Fila<T> filaPrioritaria;
    private int contadorNormaisChamadas;

public class FilaPrioritaria<T> {
    private Fila<T> filaNormal;
    private Fila<T> filaPrioritaria;
    private int contadorNormaisChamadas;

    public FilaPrioritaria() {
        this.filaNormal = new Fila<>();
        this.filaPrioritaria = new Fila<>();
        this.contadorNormaisChamadas = 0;
    }

    public static FilaPrioritaria get() {
        return singleton;

    public static FilaPrioritaria<Senha> get() {
        if (instancia == null) {
            instancia = new FilaPrioritaria<>();
        }
        return instancia;
    }

    public Fila<T> getFilaPrioritaria() {
        return filaPrioritaria;
    }

    public Fila<T> getFilaNormal() {
        return filaNormal;
    }

    public void enfileirar(T elemento, boolean ehPrioritario) {
        if (ehPrioritario) {
            filaPrioritaria.enfileirar(elemento);
        } else {
            filaNormal.enfileirar(elemento);
        }
        
    public T desenfileirar() {
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            if (contadorNormaisChamadas < 2) {
                contadorNormaisChamadas++;
                return filaNormal.desenfileirar();
            } else {
                contadorNormaisChamadas = 0;
                return filaPrioritaria.desenfileirar();
            }
        }

        if (!filaPrioritaria.estaVazia()) {
            contadorNormaisChamadas = 0;
            return filaPrioritaria.desenfileirar();
        }


        if (!filaNormal.estaVazia()) {

    public List<Senha> proximasSenhas(int quantidade) {
        return new ArrayList<>();

    public T espiarProximo() {
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            return (contadorNormaisChamadas < 2) ? filaNormal.espiar() : filaPrioritaria.espiar();
        }
        if (!filaPrioritaria.estaVazia()) return filaPrioritaria.espiar();
        return filaNormal.espiar();

            if (contadorNormaisChamadas < 2) {
                contadorNormaisChamadas++;
            }
            return filaNormal.desenfileirar();
        }

        return null;
    }


    public T espiarProximo() {
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            return (contadorNormaisChamadas < 2) ? filaNormal.espiar() : filaPrioritaria.espiar();
        }
        if (!filaPrioritaria.estaVazia()) return filaPrioritaria.espiar();
        return filaNormal.espiar();
    }

    public boolean removerElemento(T elemento) {
        if (filaNormal.removerElemento(elemento)) {
            return true;
        }
        return filaPrioritaria.removerElemento(elemento);
    }

    public boolean estaVazia() {
        return filaNormal.estaVazia() && filaPrioritaria.estaVazia();
    }

    public int getTamanhoTotal() {
        return filaNormal.getTamanho() + filaPrioritaria.getTamanho();
    }

    public int getTamanhoNormal() {
        return filaNormal.getTamanho();
    }

    public int getTamanhoPrioritaria() {
        return filaPrioritaria.getTamanho();
    }
}