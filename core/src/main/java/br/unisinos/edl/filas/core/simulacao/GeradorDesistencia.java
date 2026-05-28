package br.unisinos.edl.filas.core.simulacao;

import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.AGUARDANDO;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.DESISTENCIA;

import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.estruturas.FilaPrioritaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorDesistencia {
    private FilaPrioritaria<Senha> fila;

    public GeradorDesistencia(FilaPrioritaria<Senha> fila) {
        this.fila = fila;
    }

    public List<Senha> calcularDesistencias() {
        List<Senha> aguardando = new ArrayList<>();

        int total = fila.getTamanhoTotal();

        for (int i = 0; i < total; i++) {
            Senha s = fila.desenfileirar();

            if(s == null) {
                continue;
            }

            if (s.getStatus() == AGUARDANDO) {
                aguardando.add(s);
            }

            fila.enfileirar(s, s.ehPrioritario());
        }

        Random rand = new Random();
        List<Senha> desistentes = new ArrayList<>();

        aguardando.forEach(senha -> {
            int chanceDesistencia = rand.nextInt(101);
            if (chanceDesistencia >= 10 && chanceDesistencia <= 20) {
                senha.setStatus(DESISTENCIA);
                desistentes.add(senha);
            }
        });

        return desistentes;
    }
}

