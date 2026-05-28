package br.unisinos.edl.filas.core.estruturas;

public class FilaPrioritaria<T> {
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
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            if (contadorNormaisChamadas < 2) {
                contadorNormaisChamadas++;
                return filaNormal.desenfileirar();
            } else {
                contadorNormaisChamadas = 0;
                return filaPrioritaria.desenfileirar();
            }
        }

        if (!filaPrioritaria.estaVazia()) {
            contadorNormaisChamadas = 0;
            return filaPrioritaria.desenfileirar();
        }

        if (!filaNormal.estaVazia()) {
            if (contadorNormaisChamadas < 2) {
                contadorNormaisChamadas++;
            }
            return filaNormal.desenfileirar();
        }

        return null;
    }

    public T espiarProximo() {
        if (!filaNormal.estaVazia() && !filaPrioritaria.estaVazia()) {
            return (contadorNormaisChamadas < 2) ? filaNormal.espiar() : filaPrioritaria.espiar();
        }
        if (!filaPrioritaria.estaVazia()) return filaPrioritaria.espiar();
        return filaNormal.espiar();
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
}

