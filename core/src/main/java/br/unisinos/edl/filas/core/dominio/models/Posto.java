package br.unisinos.edl.filas.core.dominio.models;

import java.util.Optional;

public class Posto {
    private final int slot;
    private Senha senha;

    public Posto(final int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public Optional<Senha> getSenha() {
        return Optional.ofNullable(senha);
    }

    public void setSenha(final Senha senha) {
        this.senha = senha;
    }

    public boolean isEmAtendimento() {
        return senha != null;
    }

    public void ocupar(final Senha senha) {
        if (isEmAtendimento()) {
            throw new IllegalStateException("O posto já está ocupado.");
        }
        this.senha = senha;
        this.senha.senhaEmAtendimento(); // marca a senha como em atendimento
    }

    public Senha finalizarAtendimento() {
        if(!isEmAtendimento()) {
            throw new IllegalStateException("Não há senha em atendimento para finalizar.");
        }

        this.senha.senhaAtendida();
        var senhaFinalizada = this.senha;

        this.senha = null;

        return senhaFinalizada;
    }
}
