package br.unisinos.edl.filas.core.dominio;

import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoSenha;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.DESISTENCIA;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.SENHA_ATENDIDA;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.SENHA_GERADA;

import br.unisinos.edl.filas.core.dominio.models.Posto;
import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.estruturas.FilaPrioritaria;
import br.unisinos.edl.filas.core.estruturas.Pilha;
import br.unisinos.edl.filas.core.simulacao.GeradorDesistencia;
import br.unisinos.edl.filas.core.simulacao.GeradorSenha;
import br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CicloDeAtendimento {
    private static final Logger log = Logger.getLogger(CicloDeAtendimento.class.getName());
    public record ResultadoCiclo(
            List<Senha> geradas,
            List<Senha> desistencias,
            List<Senha> atendidas
    ) {
        public List<EventoCiclo> toEventos() {
            var eventos = new ArrayList<EventoCiclo>();

            eventos.addAll(geradas.stream()
                .map(senha -> eventoSenha(SENHA_GERADA, "Senha gerada: " + senha.getNumeroExibicao(), senha))
                .toList());
            eventos.addAll(desistencias.stream()
                .map(senha -> eventoSenha(DESISTENCIA, "Senha desistiu: " + senha.getNumeroExibicao(), senha))
                .toList());
            eventos.addAll(atendidas.stream()
                .map(senha -> eventoSenha(SENHA_ATENDIDA, "Senha atendida: " + senha.getNumeroExibicao(), senha))
                .toList());

            return eventos;
        }
    }

    private final FilaPrioritaria<Senha> fila;
    private final GerenciadorPosto gerenciadorPosto;
    private final GeradorSenha geradorSenha;
    private final GeradorDesistencia geradorDesistencia;

    private final Pilha<Senha> senhasAtendidas;

    public CicloDeAtendimento(
        FilaPrioritaria<Senha> fila, GerenciadorPosto gerenciadorPosto, GeradorSenha geradorSenha, GeradorDesistencia geradorDesistencia) {
        this.fila = fila;
        this.gerenciadorPosto = gerenciadorPosto;
        this.geradorSenha = geradorSenha;
        this.geradorDesistencia = geradorDesistencia;
        this.senhasAtendidas = new Pilha<>();
    }

    public Pilha<Senha> getSenhasAtendidas() {
        return senhasAtendidas;
    }

    public ResultadoCiclo executarTick() {
        List<Senha> geradas;
        List<Senha> desistencias;
        List<Senha> atendidas;

        finalizarAtendimentosAnteriores();
        geradas = gerarEEnfileirarSenhas();
        var desistentes = geradorDesistencia.calcularDesistencias();
        desistencias = removerDesistentes(desistentes);
        atendidas = ocuparPostosLivres();

        return new ResultadoCiclo(geradas, desistencias, atendidas);
    }

    private void finalizarAtendimentosAnteriores() {
        gerenciadorPosto.getPostos().stream()
            .filter(Posto::isEmAtendimento)
            .map(Posto::finalizarAtendimento)
            .forEach(senhasAtendidas::empilhar);
    }

    private List<Senha> gerarEEnfileirarSenhas() {
        List<Senha> geradas = geradorSenha.gerarSenhas();
        geradas.forEach(s -> fila.enfileirar(s, s.ehPrioritario()));
        return geradas;
    }

    private List<Senha> removerDesistentes(List<Senha> desistentes) {
        List<Senha> desistencias = new ArrayList<>();

        for(Senha senha : desistentes) {
            var removido = false;

            if(senha.ehPrioritario()) {
                removido = fila.getFilaPrioritaria().removerElemento(senha);
            } else {
                removido = fila.getFilaNormal().removerElemento(senha);
            }

            if(removido) {
                desistencias.add(senha);
            } else {
                log.warning("Senha marcada como desistente, mas não encontrada na fila: " + senha.getNumeroExibicao());
            }
        }

        return desistencias;
    }

    private List<Senha> ocuparPostosLivres() {
        List<Senha> emAtendimento = new ArrayList<>();
        for (Posto posto : gerenciadorPosto.getPostos()) {
            if (!posto.isEmAtendimento()) {
                Senha proxima = fila.desenfileirar();
                if (proxima != null) {
                    posto.ocupar(proxima);
                    emAtendimento.add(proxima);
                }
            }
        }
        return emAtendimento;
    }
}
