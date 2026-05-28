package br.unisinos.edl.filas.core.simulacao;

import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.dominio.models.Senha.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static br.unisinos.edl.filas.core.dominio.models.Senha.Tipo.NORMAL;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Tipo.PRIORITARIA;

public class GeradorSenha {
    private static final int MIN_SENHAS_POR_TICK = 0;
    private static final int MAX_SENHAS_POR_TICK = 6;
    private static final int TOTAL_SENHAS_INICIAIS = 15;
    private final Random rand = new Random();
    private int contadorNormal = 0;
    private int contadorPrioritaria = 0;

    public List<Senha> gerarIniciais() {
        List<Senha> senhas = new ArrayList<>();

        for (int i = 0; i < TOTAL_SENHAS_INICIAIS; i++) {
            var senha = gerarSenha();

            senhas.add(i, senha);
        }

        return senhas;
    }

    public List<Senha> gerarSenhas() {
        List<Senha> senhas = new ArrayList<>();
        Random rand = new Random();

        //Gera numero aleatorio de senhas novas, entre 2 e 6 senhas por tick.
        int quantSenha = rand.nextInt(MIN_SENHAS_POR_TICK, MAX_SENHAS_POR_TICK + 1);

        for (int i = 0; i <= quantSenha; i++) {
            var senha = gerarSenha();

            senhas.add(senha);
        }

        return senhas;
    }

    private Senha gerarSenha() {
        //Probabilidade entre N e P, 30% de chance de ser prioritário.
        int chance = rand.nextInt(101);

        Tipo tipo = chance <= 30 ? PRIORITARIA : NORMAL;

        int numero;

        if (tipo == PRIORITARIA) {
            numero = ++contadorPrioritaria;
        } else {
            numero = ++contadorNormal;
        }

        return new Senha(numero, tipo);
    }
}


