package br.unisinos.edl.filas.server.service;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;

import br.unisinos.edl.filas.core.simulacao.engine.SimulacaoEngine;
import br.unisinos.edl.filas.core.simulacao.engine.SimulacaoMode;
import br.unisinos.edl.filas.core.simulacao.engine.SimulacaoSnapshot;
import br.unisinos.edl.filas.server.dto.SimulacaoMapper;
import br.unisinos.edl.filas.server.dto.SimulacaoResponse;
import org.springframework.stereotype.Service;

@Service
public class SimulacaoService {
    private volatile SimulacaoEngine engine;
    private volatile SimulacaoSnapshot lastSnapshot;
    private volatile SimulacaoMode currentMode;
    private final Object lock = new Object();

    public SimulacaoResponse iniciarSimulacao(SimulacaoMode modo) {
        synchronized (lock) {
            if (engine != null && !engine.estaEncerrada()) {
                throw new IllegalStateException("Simulação já em andamento");
            }
            this.engine = new SimulacaoEngine(modo);
            this.currentMode = modo;
            this.lastSnapshot = engine.tick();
            return SimulacaoMapper.toResponse(lastSnapshot, modo);
        }
    }

    public SimulacaoResponse tick() {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            if (engine.estaEncerrada()) {
                return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
            }
            lastSnapshot = engine.tick();
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

    public SimulacaoResponse status() {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            if (engine.estaEncerrada()) {
                return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
            }
            lastSnapshot = engine.getSnapshot(emptyList());
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

    public SimulacaoResponse encerrar() {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            if (engine.estaEncerrada()) {
                return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
            }
            lastSnapshot = engine.encerrar();
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

    public SimulacaoResponse adicionarPosto() {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            var evento = engine.adicionarPosto();
            lastSnapshot = engine.getSnapshot(evento);
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

    public SimulacaoResponse removerPosto() {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            var evento = engine.removerPosto();
            lastSnapshot = engine.getSnapshot(evento);
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

    public SimulacaoResponse desistenciaIndividual(String numeroExibicao) {
        synchronized (lock) {
            if (engine == null) {
                throw new IllegalStateException("Simulação não iniciada");
            }
            var evento = engine.desistenciaIndividual(numeroExibicao);
            lastSnapshot = engine.getSnapshot(evento);
            return SimulacaoMapper.toResponse(lastSnapshot, currentMode);
        }
    }

}
