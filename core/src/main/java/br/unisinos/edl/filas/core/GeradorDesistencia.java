package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.dominio.Senha;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorDesistencia {
    public void calcularDesistencias() {
        FilaPrioritaria<Senha> fila = new FilaPrioritaria<>();
        List<Senha> aguardando = new ArrayList<>();

        int total = fila.getTamanhoTotal();

        for (int i = 0; i < total; i++) {
            Senha s = fila.desenfileirar();
            if (s.getStatus() == Senha.Status.AGUARDANDO) {
                aguardando.add(s);
            }
            // Respeitar se é prioritário ou não
            fila.enfileirar(s, s.ehPrioritario());
        }

        Random rand = new Random();
        aguardando.forEach(senha -> {
            int chanceDesistencia = rand.nextInt(101);
            if (chanceDesistencia >= 10 && chanceDesistencia <= 20) {
                senha.setStatus(Senha.Status.DESISTENCIA);
            }
        });
    }
}

