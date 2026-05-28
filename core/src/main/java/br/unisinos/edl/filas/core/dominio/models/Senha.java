package br.unisinos.edl.filas.core.dominio.models;

import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.AGUARDANDO;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.EM_ATENDIMENTO;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Status.FINALIZADO;

public class Senha {
    private final int numero;
    private final Tipo tipo;
    private Status status;

    public Senha(final int numero, final Tipo tipo) {
        this.tipo = tipo;
        this.numero = numero;
        this.status = AGUARDANDO;
    }

    public int getNumero() {
        return numero;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void senhaAtendida() {
        this.status = FINALIZADO;
    }

    public void senhaEmAtendimento() {
        this.status = EM_ATENDIMENTO;
    }

    public String getNumeroExibicao() {
        // Ex: N001 (%03d = Preencher número com 3 zeros à esquerda)
        return String.format("%s%03d", tipo.prefixo, numero);
    }

    public boolean ehPrioritario() {
        return this.tipo == Tipo.PRIORITARIA;
    }

    public enum Tipo {
        PRIORITARIA("P"), NORMAL("N");

        private final String prefixo;

        Tipo(String prefixo) {
            this.prefixo = prefixo;
        }

        public String getPrefixo() {
            return prefixo;
        }
    }

    public enum Status {
        AGUARDANDO,
        EM_ATENDIMENTO,
        FINALIZADO,
        DESISTENCIA
    }
}
