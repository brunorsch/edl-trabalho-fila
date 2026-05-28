package br.unisinos.edl.filas.core.simulacao;

import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.AGUARDANDO;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.DESISTENCIA;

import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.estruturas.Fila;
import br.unisinos.edl.filas.core.estruturas.FilaPrioritaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class GeradorDesistencia {
    private static final Logger log = Logger.getLogger(GeradorDesistencia.class.getName());
    private FilaPrioritaria<Senha> fila;
    private final Random rand = new Random();

    public GeradorDesistencia(FilaPrioritaria<Senha> fila) {
        this.fila = fila;
    }

    public List<Senha> calcularDesistencias() {
        List<Senha> aguardando = new ArrayList<>();
        aguardando.addAll(fila.toListNormal());
        aguardando.addAll(fila.toListPrioritaria());

        List<Senha> desistentes = new ArrayList<>();

        aguardando.stream()
            .filter(s -> s.getStatus() == AGUARDANDO)
            .forEach(senha -> {
                int chanceDesistencia = rand.nextInt(101);
                if (chanceDesistencia >= 10 && chanceDesistencia <= 20) {
                    senha.setStatus(DESISTENCIA);
                    desistentes.add(senha);
                }
            });

        return desistentes;
    }
}

