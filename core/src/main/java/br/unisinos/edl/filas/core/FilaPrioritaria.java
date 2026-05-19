package br.unisinos.edl.filas.core;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.edl.filas.core.dominio.Senha;

public class FilaPrioritaria {
    private final Fila<Senha> filaNormal;
    private final Fila<Senha> filaPrioridade;

    public FilaPrioritaria() {
        this.filaNormal = new Fila<>();
        this.filaPrioridade = new Fila<>();
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

    public List<Senha> proximasSenhas(int quantidade) {
        return new ArrayList<>();
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
}
