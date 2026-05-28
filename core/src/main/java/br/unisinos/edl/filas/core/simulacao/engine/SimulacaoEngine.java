package br.unisinos.edl.filas.core.simulacao.engine;

import static br.unisinos.edl.filas.core.simulacao.engine.EventoCiclo.eventoFinal;
import static br.unisinos.edl.filas.core.simulacao.engine.TipoEvento.SIMULACAO_FINALIZADA;

import br.unisinos.edl.filas.core.dominio.CicloDeAtendimento;
import br.unisinos.edl.filas.core.dominio.GerenciadorPosto;
import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.estruturas.FilaPrioritaria;
import br.unisinos.edl.filas.core.simulacao.ControladorDinamicoPostos;
import br.unisinos.edl.filas.core.simulacao.GeradorDesistencia;
import br.unisinos.edl.filas.core.simulacao.GeradorSenha;

import java.util.ArrayList;
import java.util.List;

public class SimulacaoEngine {
    private final FilaPrioritaria<Senha> fila;
    private final GerenciadorPosto gerenciadorPosto;
    private final GeradorDesistencia geradorDesistencia;
    private final CicloDeAtendimento cicloDeAtendimento;
    private final ControladorDinamicoPostos controladorDinamicoPostos;

    private int tickAtual;
    private boolean encerrada;
    private SimulacaoSnapshot snapshotFinal;

    private int totalGeradas;
    private int totalDesistencias;
    private int totalAtendidas;

    public SimulacaoEngine() {
        final GeradorSenha geradorSenha = new GeradorSenha();
        this.gerenciadorPosto = new GerenciadorPosto();

        this.fila = new FilaPrioritaria<>();
        var postos = gerenciadorPosto.getPostos();

        this.geradorDesistencia = new GeradorDesistencia(fila);
        this.cicloDeAtendimento = new CicloDeAtendimento(fila, postos, geradorSenha, geradorDesistencia);
        this.controladorDinamicoPostos = new ControladorDinamicoPostos(gerenciadorPosto);

        this.tickAtual = 0;
        this.encerrada = false;
        this.totalGeradas = 0;
        this.totalDesistencias = 0;
        this.totalAtendidas = 0;
    }

    public SimulacaoSnapshot tick() {
        if (encerrada) {
            return snapshotFinal;
        }

        var eventos = new ArrayList<EventoCiclo>();
        var postos = gerenciadorPosto.getPostos();

        var eventosPostos = controladorDinamicoPostos.decidir();
        var resultadoCiclo = cicloDeAtendimento.executarTick();
        var eventosSenhas = resultadoCiclo.toEventos();

        totalAtendidas += resultadoCiclo.atendidas().size();
        totalGeradas += resultadoCiclo.geradas().size();
        totalDesistencias += resultadoCiclo.desistencias().size();

        eventos.addAll(eventosPostos);
        eventos.addAll(eventosSenhas);

        tickAtual++;

        List<Senha> ultimasAtendidas = cicloDeAtendimento.getSenhasAtendidas().paraLista(10);
        List<Senha> proximasDuas = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Senha proxima = fila.espiarProximo();
            if (proxima != null) {
                proximasDuas.add(proxima);
            }
        }

        return new SimulacaoSnapshot(
                tickAtual,
                fila.toListNormal(),
                fila.toListPrioritaria(),
                proximasDuas,
                postos,
                ultimasAtendidas,
                totalAtendidas,
                totalGeradas,
                totalDesistencias,
                eventos,
                false
        );
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

    public boolean isEncerrada() {
        return encerrada;
    }
}
