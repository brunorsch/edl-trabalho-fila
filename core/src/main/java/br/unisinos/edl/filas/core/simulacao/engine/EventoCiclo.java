package br.unisinos.edl.filas.core.simulacao.engine;

import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.SIMULACAO_FINALIZADA;
import static java.lang.String.format;

import br.unisinos.edl.filas.core.dominio.models.Posto;
import br.unisinos.edl.filas.core.dominio.models.Senha;

public record EventoCiclo(
        TipoEvento tipo,
        String descricao,
        Senha senha,
        Posto posto
) {
    public static EventoCiclo eventoSenha(TipoEvento tipo, String descricao, Senha senha) {
        return new EventoCiclo(tipo, descricao, senha, null);
    }

    public static EventoCiclo eventoPosto(TipoEvento tipo, String descricao, Posto posto) {
        return new EventoCiclo(tipo, descricao, null, posto);
    }

    public static EventoCiclo eventoFinal(int totalAtendidas, int totalGeradas, int totalDesistencias) {
        return new EventoCiclo(
            SIMULACAO_FINALIZADA,
            format("Simulação finalizada. %d atendidas, %d geradas, %d desistências",
                totalAtendidas, totalGeradas, totalDesistencias),
            null,
            null
        );
    }
}
