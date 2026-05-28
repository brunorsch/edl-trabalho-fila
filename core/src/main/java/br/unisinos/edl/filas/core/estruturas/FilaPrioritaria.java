package br.unisinos.edl.filas.core.estruturas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FilaPrioritaria<T> {
    private static final Logger log = Logger.getLogger(FilaPrioritaria.class.getName());
    private Fila<T> filaNormal;
    private Fila<T> filaPrioritaria;
    private int contadorNormaisChamadas;

    public FilaPrioritaria() {
        this.filaNormal = new Fila<>();
        this.filaPrioritaria = new Fila<>();
        this.contadorNormaisChamadas = 0;
    }

    public Fila<T> getFilaPrioritaria() {
        return filaPrioritaria;
    }

    public Fila<T> getFilaNormal() {
        return filaNormal;
    }

    public void enfileirar(T elemento, boolean ehPrioritario) {
        if (ehPrioritario) {
            filaPrioritaria.enfileirar(elemento);
        } else {
            filaNormal.enfileirar(elemento);
        }
    }

    public T desenfileirar() {
        boolean temNormal = !filaNormal.estaVazia();
        boolean temPrioritaria = !filaPrioritaria.estaVazia();

        if (temNormal && temPrioritaria) {
            if (contadorNormaisChamadas < 2) {
                contadorNormaisChamadas++;
                return filaNormal.desenfileirar();
            } else {
                contadorNormaisChamadas = 0;
                return filaPrioritaria.desenfileirar();
            }
        }

        if (temPrioritaria) {
            contadorNormaisChamadas = 0;
            return filaPrioritaria.desenfileirar();
        }

        if (temNormal) {
            contadorNormaisChamadas++;
            return filaNormal.desenfileirar();
        }

        return null;
    }

    public List<T> espiarProximas(int quantidade) {
        var proximas = new ArrayList<T>();
        int idxNormal = 0;
        int idxPrioritaria = 0;
        int contadorSimulado = contadorNormaisChamadas;

        for (int i = 0; i < quantidade; i++) {
            boolean temNormal = filaNormal.getTamanho() > idxNormal;
            boolean temPrioritaria = filaPrioritaria.getTamanho() > idxPrioritaria;

            if (temNormal && temPrioritaria) {
                if (contadorSimulado < 2) {
                    proximas.add(filaNormal.espiar(idxNormal++));
                    contadorSimulado++;
                } else {
                    proximas.add(filaPrioritaria.espiar(idxPrioritaria++));
                    contadorSimulado = 0;
                }
            } else if (temPrioritaria) {
                proximas.add(filaPrioritaria.espiar(idxPrioritaria++));
                contadorSimulado = 0;
            } else if (temNormal) {
                proximas.add(filaNormal.espiar(idxNormal++));
                contadorSimulado++;
            } else {
                break;
            }
        }

        return proximas;
    }

    public List<T> espiarProximasDuas() {
        return espiarProximas(2);
    }

    public boolean removerElemento(T elemento) {
        if (filaNormal.removerElemento(elemento)) {
            return true;
        }
        return filaPrioritaria.removerElemento(elemento);
    }

    public boolean estaVazia() {
        return filaNormal.estaVazia() && filaPrioritaria.estaVazia();
    }

    public int getTamanhoTotal() {
        return filaNormal.getTamanho() + filaPrioritaria.getTamanho();
    }

    public int getTamanhoNormal() {
        return filaNormal.getTamanho();
    }

    public int getTamanhoPrioritaria() {
        return filaPrioritaria.getTamanho();
    }

    public List<T> toListNormal() {
        return filaNormal.toList();
    }

    public List<T> toListPrioritaria() {
        return filaPrioritaria.toList();
    }
}

