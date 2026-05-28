package br.unisinos.edl.filas.core.simulacao;

import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoPosto;

import br.unisinos.edl.filas.core.dominio.GerenciadorPosto;
import br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo;
import br.unisinos.edl.filas.core.simulacao.engine.TipoEvento;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControladorDinamicoPostos {
    private final GerenciadorPosto gerenciador;
    private final Random random = new Random();

    public ControladorDinamicoPostos(final GerenciadorPosto gerenciador) {
        this.gerenciador = gerenciador;
    }

    public List<EventoCiclo> decidir() {
        List<EventoCiclo> eventos = new ArrayList<>();
        int totalPostos = gerenciador.getPostos().size();

        // Probabilidade de 20% de ativar um posto se houver menos de 5
        if (totalPostos < 5 && random.nextInt(100) < 20) {
            int slot = totalPostos + 1;
            gerenciador.ativarPosto(slot);
            eventos.add(eventoPosto(TipoEvento.POSTO_ATIVADO, "Posto " + slot + " ativado", gerenciador.getPostos().get(slot - 1)));
        }

        // Probabilidade de 15% de desativar um posto se houver mais de 3
        if (totalPostos > 3 && random.nextInt(100) < 15) {
            int slot = totalPostos;
            var postoDesativado = gerenciador.getPostos().get(slot - 1);
            gerenciador.desativarPosto(slot);
            eventos.add(eventoPosto(TipoEvento.POSTO_DESATIVADO, "Posto " + slot + " desativado", postoDesativado));
        }

        return eventos;
    }
}
