package br.unisinos.edl.filas.core;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.edl.filas.core.dominio.Senha;

<<<<<<< Updated upstream
public class FilaPrioritaria {
    private static FilaPrioritaria singleton;
    private int proximoNumero = 1;
    private final Fila<Senha> filaNormal;
    private final Fila<Senha> filaPrioridade;
=======
public class FilaPrioritaria<T> {
    private static FilaPrioritaria<Senha> instancia; //Criar singleton
    private Fila<T> filaNormal;
    private Fila<T> filaPrioritaria;
    private int contadorNormaisChamadas;
>>>>>>> Stashed changes

    public FilaPrioritaria() {
        this.filaNormal = new Fila<>();
        this.filaPrioridade = new Fila<>();
    }

<<<<<<< Updated upstream
    public static FilaPrioritaria get() {
        return singleton;
=======
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
>>>>>>> Stashed changes
    }

    public Fila<Senha> getFilaNormal() {
        return filaNormal;
    }

    public Fila<Senha> getFilaPrioridade() {
        return filaPrioridade;
    }

    public void enfileirar(Senha senha) {
        // TODO: Implementar
    }

    public Senha proximaSenha() {
        // TODO: Implementar
        return null;
    }

<<<<<<< Updated upstream
    public List<Senha> proximasSenhas(int quantidade) {
        return new ArrayList<>();
=======
    public T espiarProximo() {
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            return (contadorNormaisChamadas < 2) ? filaNormal.espiar() : filaPrioritaria.espiar();
        }
        if (!filaPrioritaria.estaVazia()) return filaPrioritaria.espiar();
        return filaNormal.espiar();
>>>>>>> Stashed changes
    }

    public void removerPorNumero(int numeroSenha) {
        // TODO: Implementar esse ou o de baixo (O de baixo é mais top)
    }

    public void remover(Senha senha) {
        // TODO: Implementar esse ou o de cima (Esse é mais top)
    }

    public int totalAguardando() {
        return 0; // TODO: Implementar
    }

    public int totalNormal() {
        return 0; // TODO: Implementar
    }

    public int totalPrioridade() {
        return 0; // TODO: Implementar
    }

    public int espiarProximoNumero() {
        return proximoNumero;
    }

    public int proximoNumero() {
        return proximoNumero++;
    }
}
