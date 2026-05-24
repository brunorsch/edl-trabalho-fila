package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.dominio.Senha;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorDesistencia {
    public void calcularDesistencias() {
        FilaPrioritaria fila = FilaPrioritaria.get();
        List<Senha> aguardando = new ArrayList<>();

        int totalNormal = fila.totalNormal();
        int totalPrioridade = fila.totalPrioridade();

        for(int i = 0; i < totalNormal; i++){
            Senha s = fila.getFilaNormal().desenfileirar();
            if (s.getStatus() == Senha.Status.AGUARDANDO) {
                aguardando.add(s);
            }
            fila.enfileirar(s);
        }

        for (int i = 0; i < totalPrioridade; i++) {
            Senha s1 = fila.getFilaPrioridade().desenfileirar();
                if (s1.getStatus() == Senha.Status.AGUARDANDO) {
                    aguardando.add(s1);
                }
                fila.enfileirar(s1);
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
