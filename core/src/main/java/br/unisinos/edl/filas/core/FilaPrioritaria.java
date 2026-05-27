package br.unisinos.edl.filas.core;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.edl.filas.core.dominio.Senha;

public class FilaPrioritaria {
    private static FilaPrioritaria singleton;
    private final Fila<Senha> filaNormal;
    private final Fila<Senha> filaPrioritaria;
    private int contadorNormaisChamadas;

    private FilaPrioritaria() {
        this.filaNormal = new Fila<>();
        this.filaPrioritaria = new Fila<>();
        this.contadorNormaisChamadas = 0;
    }

    public static FilaPrioritaria getInstance() {
        if (singleton == null) {
            singleton = new FilaPrioritaria();
        }
        return singleton;
    }

    public Fila<Senha> getFilaPrioritaria() {
        return filaPrioritaria;
    }

    public Fila<Senha> getFilaNormal() {
        return filaNormal;
    }

    public void enfileirar(Senha elemento, boolean ehPrioritario) {
        if (ehPrioritario) {
            filaPrioritaria.enfileirar(elemento);
        } else {
            filaNormal.enfileirar(elemento);
        }
    }

    public Senha desenfileirar() {
        if (filaNormal.estaVazia() && filaPrioritaria.estaVazia()) {
            return null;
        }

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
            contadorNormaisChamadas++;
            return filaNormal.desenfileirar();
        }

        return null;
    }

    public List<Senha> proximasSenhas(int quantidade) {
        List<Senha> senhas = new ArrayList<>();
        int count = 0;
        
        while (count < quantidade && (!filaNormal.estaVazia() || !filaPrioritaria.estaVazia())) {
            Senha proxima = desenfileirar();
            if (proxima != null) {
                senhas.add(proxima);
                count++;
            }
        }
        
        return senhas;
    }

    public Senha espiarProximo() {
        if (filaNormal.estaVazia() && filaPrioritaria.estaVazia()) {
            return null;
        }

        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            return (contadorNormaisChamadas < 2) ? filaNormal.espiar() : filaPrioritaria.espiar();
        }
        
        if (!filaPrioritaria.estaVazia()) {
            return filaPrioritaria.espiar();
        }
        
        return filaNormal.espiar();
    }

    public boolean removerElemento(Senha elemento) {
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