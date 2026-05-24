package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.dominio.Posto;
import br.unisinos.edl.filas.core.dominio.Senha;

import java.util.ArrayList;
import java.util.Optional;

public class GerenciadorPosto {

    private ArrayList<Posto> postos;

    public GerenciadorPosto() {
        postos = new ArrayList<>();
        postos.add(new Posto(1));
        postos.add(new Posto(2));
        postos.add(new Posto(3));
    }

    public Optional<Posto> getPostoLivre() {
        return postos.stream()
                .filter(Posto::isEmAtendimento)
                .findFirst();
    }

    public void ativarPosto(int slot) {
        if (postos.size() < 5 && slot <= 5 && !isSlotOcupado(slot)) {
            postos.add(new Posto(slot));
        }
    }

    private boolean isSlotOcupado(int slot) {
        return postos.stream().anyMatch(posto -> posto.getSlot() == slot);
    }

    public void desativarPosto(int slot) {
        if (postos.size() > 3) {
            postos.stream()
                    .filter(posto -> posto.getSlot() == slot)
                    .findFirst()
                    .ifPresent(posto -> postos.remove(posto));
        }
    }

    public void ocuparPosto(int slot, Senha senha) {
        postos.stream()
                .filter(posto -> posto.getSlot() == slot)
                .findFirst()
                .ifPresent(posto -> posto.setSenha(senha));
    }

    public void liberarPosto(int slot) {
        postos.stream()
                .filter(posto -> posto.getSlot() == slot)
                .findFirst()
                .ifPresent(Posto::finalizarAtendimento);
    }

    public void listarPostos() {
        postos.forEach(posto -> System.out.println(posto.toString()));
    }
}
