package br.unisinos.edl.filas.server.dto;
import br.unisinos.edl.filas.core.simulacao.engine.*;
import br.unisinos.edl.filas.core.dominio.models.*;
import java.util.List;

public class SimulacaoMapper {
    public static SimulacaoResponse toResponse(SimulacaoSnapshot snapshot, SimulacaoMode mode) {
        return new SimulacaoResponse(
            snapshot.tickAtual(),
            mapSenhas(snapshot.normal()),
            mapSenhas(snapshot.prioritaria()),
            mapSenhas(snapshot.proximasDuas()),
            mapPostos(snapshot.postos()),
            mapSenhas(snapshot.atendidas()),
            snapshot.totalAtendidas(),
            snapshot.totalGeradas(),
            snapshot.totalDesistencias(),
            mapEventos(snapshot.eventosUltimoCiclo()),
            snapshot.encerrada(),
            mode.name()
        );
    }
    
    private static List<SenhaDTO> mapSenhas(List<Senha> senhas) {
        return senhas.stream().map(SimulacaoMapper::toSenhaDTO).toList();
    }
    
    private static SenhaDTO toSenhaDTO(Senha senha) {
        return new SenhaDTO(
            senha.getNumero(),
            senha.getTipo().name(),
            senha.getStatus().name(),
            senha.getNumeroExibicao(),
            senha.ehPrioritario()
        );
    }
    
    private static List<PostoDTO> mapPostos(List<Posto> postos) {
        return postos.stream().map(SimulacaoMapper::toPostoDTO).toList();
    }
    
    private static PostoDTO toPostoDTO(Posto posto) {
        return new PostoDTO(
            posto.getSlot(),
            posto.isEmAtendimento(),
            posto.getSenha().map(SimulacaoMapper::toSenhaDTO).orElse(null)
        );
    }
    
    private static List<EventoCicloDTO> mapEventos(List<EventoCiclo> eventos) {
        return eventos.stream().map(SimulacaoMapper::toEventoDTO).toList();
    }
    
    private static EventoCicloDTO toEventoDTO(EventoCiclo evento) {
        return new EventoCicloDTO(
            evento.tipo().name(),
            evento.descricao(),
            evento.senha() != null ? toSenhaDTO(evento.senha()) : null,
            evento.posto() != null ? toPostoDTO(evento.posto()) : null
        );
    }
}