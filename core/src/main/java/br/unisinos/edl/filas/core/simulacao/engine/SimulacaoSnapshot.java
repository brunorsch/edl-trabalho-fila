package br.unisinos.edl.filas.core.simulacao.engine;

import br.unisinos.edl.filas.core.dominio.models.Posto;
import br.unisinos.edl.filas.core.dominio.models.Senha;

import java.util.List;

public record SimulacaoSnapshot(
        int tickAtual,
        List<Senha> normal,
        List<Senha> prioritaria,
        List<Senha> proximasDuas,
        List<Posto> postos,
        List<Senha> atendidas,
        int totalAtendidas,
        int totalGeradas,
        int totalDesistencias,
        List<EventoCiclo> eventosUltimoCiclo,
        boolean encerrada
) {
}
