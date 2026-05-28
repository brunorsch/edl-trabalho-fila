package br.unisinos.edl.filas.server.controller;

import br.unisinos.edl.filas.core.simulacao.engine.SimulacaoMode;
import br.unisinos.edl.filas.server.dto.IniciarRequest;
import br.unisinos.edl.filas.server.dto.SimulacaoResponse;
import br.unisinos.edl.filas.server.service.SimulacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulacao")
public class SimulacaoController {
    private final SimulacaoService service;

    public SimulacaoController(SimulacaoService service) {
        this.service = service;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<SimulacaoResponse> iniciar(@RequestBody IniciarRequest request) {
        SimulacaoMode modo = SimulacaoMode.valueOf(request.modo());
        return ResponseEntity.ok(service.iniciarSimulacao(modo));
    }

    @PostMapping("/tick")
    public ResponseEntity<SimulacaoResponse> tick() {
        return ResponseEntity.ok(service.tick());
    }

    @GetMapping("/status")
    public ResponseEntity<SimulacaoResponse> status() {
        return ResponseEntity.ok(service.status());
    }

    @PostMapping("/encerrar")
    public ResponseEntity<SimulacaoResponse> encerrar() {
        return ResponseEntity.ok(service.encerrar());
    }

    @PostMapping("/posto/adicionar")
    public ResponseEntity<SimulacaoResponse> adicionarPosto() {
        return ResponseEntity.ok(service.adicionarPosto());
    }

    @PostMapping("/posto/remover")
    public ResponseEntity<SimulacaoResponse> removerPosto() {
        return ResponseEntity.ok(service.removerPosto());
    }

    @PostMapping("/senha/{numeroExibicao}/desistencia")
    public ResponseEntity<SimulacaoResponse> desistencia(@PathVariable String numeroExibicao) {
        return ResponseEntity.ok(service.desistenciaIndividual(numeroExibicao));
    }
}
