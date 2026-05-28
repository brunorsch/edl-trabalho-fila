package br.unisinos.edl.filas.core.simulacao;

import br.unisinos.edl.filas.core.dominio.models.Senha;
import br.unisinos.edl.filas.core.dominio.models.Senha.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static br.unisinos.edl.filas.core.dominio.models.Senha.Tipo.NORMAL;
import static br.unisinos.edl.filas.core.dominio.models.Senha.Tipo.PRIORITARIA;

public class GeradorSenha {
    private int contadorNormal = 0;
    private int contadorPrioritaria = 0;

    public List<Senha> gerarSenha() {
        List<Senha> senhas = new ArrayList<Senha>();
        Random rand = new Random();
        //Gera numero aleatorio entre 0 e 2.
        int quantSenha = rand.nextInt(3);

        for (int i = 0; i <= quantSenha; i++) {
            //Probabilidade entre N e P.
            int chance = rand.nextInt(101);
            Tipo tipo = chance <= 30 ? PRIORITARIA : NORMAL;

            int numero;

            if (tipo == Senha.Tipo.PRIORITARIA) {
                numero = ++contadorPrioritaria;
            } else {
                numero = ++contadorNormal;
            }

            senhas.add(new Senha(numero, tipo));
        }
        return senhas;
    }
}


