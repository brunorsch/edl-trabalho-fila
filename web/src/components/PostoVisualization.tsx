import { Monitor, Plus, Minus, CheckCircle2 } from "lucide-react";
import type { PostoDTO, SenhaDTO } from "../types";

interface PostoVisualizationProps {
  postos: PostoDTO[];
  atendidas: SenhaDTO[];
  isManual: boolean;
  encerrada: boolean;
  onAddPosto: () => void;
  onRemovePosto: () => void;
}

export function PostoVisualization({
  postos,
  atendidas,
  isManual,
  encerrada,
  onAddPosto,
  onRemovePosto,
}: PostoVisualizationProps) {
  const atendidasParaExibir = encerrada
    ? [...atendidas].reverse()
    : atendidas.slice(-10).reverse();
  return (
    <section className="lg:col-span-4">
      <div className="rounded-xl bg-white/5 border border-white/10 p-4 h-full">
        <div className="flex items-center gap-2 mb-4">
          <Monitor size={16} className="text-white/60" />
          <h2 className="text-sm font-semibold uppercase tracking-wider text-white/70">
            Postos de Atendimento
          </h2>
          <span className="ml-auto text-xs text-white/40 font-mono">
            {postos.length} {postos.length === 1 ? "posto" : "postos"}
          </span>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
          {postos.map((posto) => (
            <div
              key={posto.slot}
              className={`rounded-lg border p-3 text-center transition-colors ${
                posto.emAtendimento && posto.senha
                  ? "bg-emerald-500/10 border-emerald-500/30"
                  : "bg-white/5 border-white/10"
              }`}
            >
              <div className="text-xs text-white/40 font-mono mb-1">
                Posto {posto.slot}
              </div>
              {posto.emAtendimento && posto.senha ? (
                <div className="text-sm font-semibold font-mono">
                  <span
                    className={
                      posto.senha.ehPrioritario
                        ? "text-purple-300"
                        : "text-emerald-300"
                    }
                  >
                    {posto.senha.numeroExibicao}
                  </span>
                </div>
              ) : (
                <div className="text-xs text-white/30">Disponível</div>
              )}
            </div>
          ))}
        </div>
        <div className="mt-4 pt-4 border-t border-white/10">
          <div className="flex items-center gap-2 mb-2">
            <CheckCircle2 size={14} className="text-emerald-400/60" />
            <h3 className="text-xs font-semibold uppercase tracking-wider text-white/50">
              {encerrada ? "Todas as senhas atendidas" : "Últimas atendidas"}
            </h3>
            <span className="ml-auto text-xs text-white/30 font-mono">
              {encerrada ? atendidasParaExibir.length : `${atendidasParaExibir.length} / 10`}
            </span>
          </div>
          {atendidasParaExibir.length > 0 ? (
            <div className="flex flex-wrap gap-1.5">
              {atendidasParaExibir.map((senha) => (
                <span
                  key={senha.numeroExibicao}
                  className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-mono font-medium ${
                    senha.ehPrioritario
                      ? "bg-purple-500/20 text-purple-300 border border-purple-500/30"
                      : "bg-emerald-500/20 text-emerald-300 border border-emerald-500/30"
                  }`}
                >
                  {senha.numeroExibicao}
                </span>
              ))}
            </div>
          ) : (
            <p className="text-xs text-white/25 italic">Nenhuma senha atendida ainda</p>
          )}
        </div>

        {isManual && (
          <div className="flex gap-2 mt-4">
            <button
              onClick={onAddPosto}
              className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-white/10 hover:bg-white/15 text-white/80 text-sm transition-colors"
            >
              <Plus size={14} />
              Adicionar Posto
            </button>
            <button
              onClick={onRemovePosto}
              className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-white/10 hover:bg-white/15 text-white/80 text-sm transition-colors"
            >
              <Minus size={14} />
              Remover Posto
            </button>
          </div>
        )}
      </div>
    </section>
  );
}