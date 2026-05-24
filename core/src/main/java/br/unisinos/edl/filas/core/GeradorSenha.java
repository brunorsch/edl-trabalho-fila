package br.unisinos.edl.filas.core;

import br.unisinos.edl.filas.core.dominio.Senha;
import br.unisinos.edl.filas.core.dominio.Senha.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static br.unisinos.edl.filas.core.dominio.Senha.Tipo.NORMAL;
import static br.unisinos.edl.filas.core.dominio.Senha.Tipo.PRIORITARIA;

public class GeradorSenha {
    public List<Senha> gerarSenha() {
        List<Senha> senhas = new ArrayList<Senha>();
        Random rand = new Random();
        //Gera numero aleatorio entre 0 e 2.
        int quantSenha = rand.nextInt(3);

        for(int i = 0; i <= quantSenha; i++){
            //Probabilidade entre N e P.
            int chance = rand.nextInt(101);
            Tipo tipo = chance <= 30 ? PRIORITARIA : NORMAL;

            senhas.add(new Senha(FilaPrioritaria.get().proximoNumero(), tipo));
        }
        return senhas;
    }
}



