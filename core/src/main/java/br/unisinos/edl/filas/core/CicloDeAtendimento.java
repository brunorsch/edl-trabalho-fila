package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.dominio.Posto;
import br.unisinos.edl.filas.core.dominio.Senha;

import java.util.ArrayList;
import java.util.List;

public class CicloDeAtendimento {
    public record ResultadoCiclo(
            List<Senha> geradas,
            List<Senha> desistencia,
            List<Senha> atendidas
    ) {}

    private final FilaPrioritaria<Senha> fila;
    private final GeradorSenha geradorSenha;
    private final GeradorDesistencia geradorDesistencia;
    private final List<Senha> pilhaAtendidas;

    public CicloDeAtendimento(FilaPrioritaria<Senha> fila) {
        this.fila = fila;
        this.geradorSenha = new GeradorSenha();
        this.geradorDesistencia = new GeradorDesistencia();
        this.pilhaAtendidas = new ArrayList<>();
    }

    public ResultadoCiclo executarTick(List<Posto> postos) {
        List<Senha> geradas = new ArrayList<>();
        List<Senha> desistencias = new ArrayList<>();
        List<Senha> atendidas = new ArrayList<>();

        // 1. Finaliza atendimentos do tick anterior
        postos.forEach(Posto::finalizarAtendimento);

        // 2. Gera novas senhas e enfileira
        geradas = geradorSenha.gerarSenha();
        geradas.forEach(s -> fila.enfileirar(s, s.ehPrioritario()));

        // 3. Processa desistências
        geradorDesistencia.calcularDesistencias();

        //Desistencias filas normal e prioritária
        No<Senha> atual = fila.getFilaNormal().getInicio();
        while (atual != null) {
            Senha s = atual.getDado();
            if (s.getStatus() == Senha.Status.DESISTENCIA) {
                desistencias.add(s);
            }
            atual = atual.getProximo();
        }

        atual = fila.getFilaPrioritaria().getInicio();
        while (atual != null) {
            Senha s = atual.getDado();
            if (s.getStatus() == Senha.Status.DESISTENCIA) {
                desistencias.add(s);
            }
            atual = atual.getProximo();
        }

        desistencias.forEach(fila::removerElemento);


        // 4. Para cada posto ativo e livre: chama próxima senha
        for (Posto posto : postos) {
            if (!posto.isEmAtendimento()) {
                Senha proxima = fila.desenfileirar();
                if (proxima != null) {
                    proxima.setStatus(Senha.Status.EM_ATENDIMENTO);
                    posto.ocupar(proxima);
                    atendidas.add(proxima);
                }
            }
        }

        // 5. Empilha senhas chamadas na pilha de atendidas com status FINALIZADO
        atendidas.forEach(s -> {
            s.setStatus(Senha.Status.FINALIZADO);
            pilhaAtendidas.add(s);
        });

        // 6. Retorna resultado do ciclo
        return new ResultadoCiclo(geradas, desistencias, atendidas);
    }
}
