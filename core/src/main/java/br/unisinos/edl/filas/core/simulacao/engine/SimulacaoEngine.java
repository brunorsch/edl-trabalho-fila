package br.unisinos.edl.filas.core.simulacao.engine;

import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoFinal;
import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoPosto;
import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoSenha;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.POSTO_ATIVADO;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.POSTO_DESATIVADO;
import static java.util.Collections.emptyList;

import br.unisinos.edl.filas.core.dominio.CicloDeAtendimento;
import br.unisinos.edl.filas.core.dominio.GerenciadorPosto;
import br.unisinos.edl.filas.core.dominio.models.Posto;
import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.dominio.models.Senha.Status;
import br.unisinos.edl.filas.core.estruturas.FilaPrioritaria;
import br.unisinos.edl.filas.core.simulacao.ControladorDinamicoPostos;
import br.unisinos.edl.filas.core.simulacao.GeradorDesistencia;
import br.unisinos.edl.filas.core.simulacao.GeradorSenha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulacaoEngine {
    private final FilaPrioritaria<Senha> fila;
    private final GerenciadorPosto gerenciadorPosto;
    private final CicloDeAtendimento cicloDeAtendimento;
    private final ControladorDinamicoPostos controladorDinamicoPostos;
    private final SimulacaoMode mode;

    private int tickAtual;
    private boolean encerrada;
    private SimulacaoSnapshot snapshotFinal;

    private int totalGeradas;
    private int totalDesistencias;
    private int totalAtendidas;

    public SimulacaoEngine() {
        this(SimulacaoMode.AUTO);
    }

    public SimulacaoEngine(SimulacaoMode mode) {
        this.tickAtual = 0;
        this.encerrada = false;
        this.totalGeradas = 0;
        this.totalDesistencias = 0;
        this.totalAtendidas = 0;

        this.fila = new FilaPrioritaria<>();
        final GeradorDesistencia geradorDesistencia = new GeradorDesistencia(fila);
        final GeradorSenha geradorSenha = new GeradorSenha();
        this.gerenciadorPosto = new GerenciadorPosto();

        var senhasInicias = geradorSenha.gerarIniciais();
        totalGeradas += senhasInicias.size();
        senhasInicias.forEach(senha -> fila.enfileirar(senha, senha.ehPrioritario()));

        this.cicloDeAtendimento = new CicloDeAtendimento(fila, gerenciadorPosto, geradorSenha, geradorDesistencia);
        this.mode = mode;
        if (mode == SimulacaoMode.MANUAL) {
            this.controladorDinamicoPostos = null;
        } else {
            this.controladorDinamicoPostos = new ControladorDinamicoPostos(gerenciadorPosto);
        }
    }

    public SimulacaoSnapshot tick() {
        if (encerrada) {
            return snapshotFinal;
        }

        var eventos = new ArrayList<EventoCiclo>();

        List<EventoCiclo> eventosPostos = (mode == SimulacaoMode.AUTO && controladorDinamicoPostos != null)
                ? controladorDinamicoPostos.decidir()
                : emptyList();
        var resultadoCiclo = cicloDeAtendimento.executarTick();
        var eventosSenhas = resultadoCiclo.toEventos();

        totalAtendidas += resultadoCiclo.atendidas().size();
        totalGeradas += resultadoCiclo.geradas().size();
        totalDesistencias += resultadoCiclo.desistencias().size();

        eventos.addAll(eventosPostos);
        eventos.addAll(eventosSenhas);

        tickAtual++;

        return getSnapshot(eventos);
    }

    public SimulacaoSnapshot encerrar() {
        if (encerrada) {
            return snapshotFinal;
        }

        SimulacaoSnapshot ultimoSnapshot = tick();

        List<Senha> atendidas = cicloDeAtendimento.getSenhasAtendidas().paraLista();

        List<EventoCiclo> eventosFinais = new ArrayList<>(ultimoSnapshot.eventosUltimoCiclo());
        eventosFinais.add(eventoFinal(totalGeradas, totalAtendidas, totalDesistencias));

        snapshotFinal = new SimulacaoSnapshot(
                tickAtual,
                fila.toListNormal(),
                fila.toListPrioritaria(),
                ultimoSnapshot.proximasDuas(),
                gerenciadorPosto.getPostos(),
                atendidas,
                totalAtendidas,
                totalGeradas,
                totalDesistencias,
                eventosFinais,
                true
        );

        encerrada = true;
        return snapshotFinal;
    }

    public EventoCiclo desistenciaIndividual(String numeroExibicao) {
        var todasSenhas = new ArrayList<Senha>();
        todasSenhas.addAll(fila.getFilaNormal().toList());
        todasSenhas.addAll(fila.getFilaPrioritaria().toList());

        for (Senha senha : todasSenhas) {
            if (senha.getNumeroExibicao().equals(numeroExibicao)) {
                senha.setStatus(Status.DESISTENCIA);
                fila.removerElemento(senha);
                totalDesistencias++;
                return eventoSenha(TipoEvento.DESISTENCIA, "Senha desistiu: " + numeroExibicao, senha);
            }
        }

        throw new IllegalArgumentException("Senha não encontrada: " + numeroExibicao);
    }

    public EventoCiclo adicionarPosto() {
        if (mode != SimulacaoMode.MANUAL) {
            throw new IllegalStateException("Apenas em modo MANUAL");
        }
        var postos = gerenciadorPosto.getPostos();
        int max = 0;
        for (var posto : postos) {
            if (posto.getSlot() > max) {
                max = posto.getSlot();
            }
        }
        if (max >= 5) {
            throw new IllegalStateException("Máximo de postos atingido");
        }
        int novoSlot = max + 1;
        gerenciadorPosto.ativarPosto(novoSlot);

        var postoAdicionado = gerenciadorPosto.getPosto(novoSlot).orElse(new Posto(novoSlot));
        return eventoPosto(POSTO_ATIVADO, "Posto " + novoSlot + " ativado", postoAdicionado);
    }

    public EventoCiclo removerPosto() {
        if (mode != SimulacaoMode.MANUAL) {
            throw new IllegalStateException("Apenas em modo MANUAL");
        }
        var postos = gerenciadorPosto.getPostos();
        if (postos.size() <= 3) {
            throw new IllegalStateException("Mínimo de postos atingido");
        }
        int maxSlot = 0;
        Posto postoRemovido = gerenciadorPosto.desativarPosto(maxSlot);
        return eventoPosto(POSTO_DESATIVADO, "Posto " + maxSlot + " desativado", postoRemovido);
    }

    public boolean estaEncerrada() {
        return encerrada;
    }

    public SimulacaoMode getMode() {
        return mode;
    }

    public SimulacaoSnapshot getSnapshot(List<EventoCiclo> eventos) {
        var postos = gerenciadorPosto.getPostos();
        int quantidadeProximas = Math.max(3, postos.size());

        return new SimulacaoSnapshot(
            tickAtual,
            fila.toListNormal(),
            fila.toListPrioritaria(),
            fila.espiarProximas(quantidadeProximas),
            postos,
            getUltimasSenhasAtendidas(),
            totalAtendidas,
            totalGeradas,
            totalDesistencias,
            eventos,
            this.encerrada
        );
    }

    public SimulacaoSnapshot getSnapshot(EventoCiclo eventoCiclo) {
        return getSnapshot(List.of(eventoCiclo));
    }

    public List<Senha> getUltimasSenhasAtendidas() {
        return cicloDeAtendimento.getSenhasAtendidas().paraLista(10);
    }
}
