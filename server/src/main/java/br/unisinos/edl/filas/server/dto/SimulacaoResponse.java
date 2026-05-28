package br.unisinos.edl.filas.server.dto;
import java.util.List;
public record SimulacaoResponse(
    int tickAtual,
    List<SenhaDTO> normal,
    List<SenhaDTO> prioritaria,
    List<SenhaDTO> proximasDuas,
    List<PostoDTO> postos,
    List<SenhaDTO> atendidas,
    int totalAtendidas,
    int totalGeradas,
    int totalDesistencias,
    List<EventoCicloDTO> eventosUltimoCiclo,
    boolean encerrada,
    String modo
) {}