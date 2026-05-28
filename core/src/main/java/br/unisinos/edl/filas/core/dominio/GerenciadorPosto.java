package br.unisinos.edl.filas.core.dominio;

import br.unisinos.edl.filas.core.dominio.models.Posto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorPosto {
    private ArrayList<Posto> postos;

    public GerenciadorPosto() {
        postos = new ArrayList<>();
        postos.add(new Posto(1));
        postos.add(new Posto(2));
        postos.add(new Posto(3));
    }

    public Posto ativarPosto(int slot) {
        if (postos.size() < 5 && slot <= 5 && !isSlotOcupado(slot)) {
            var posto = new Posto(slot);
            postos.add(posto);
            return posto;
        }
        return null;
    }

    private boolean isSlotOcupado(int slot) {
        return getPosto(slot).isPresent();
    }

    public Posto desativarPosto(int slot) {
        if (postos.size() > 3) {
            return getPosto(slot).map(posto -> {
                postos.remove(posto);
                return posto;
            }).orElse(null);
        } else return null;
    }

    public Optional<Posto> getPosto(int slot) {
        return postos.stream()
                .filter(posto -> posto.getSlot() == slot)
                .findFirst();
    }

    public List<Posto> getPostos() {
        return postos;
    }
}
