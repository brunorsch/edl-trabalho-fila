import { Monitor, Plus, Minus } from "lucide-react";
import type { PostoDTO } from "../types";

interface PostoVisualizationProps {
  postos: PostoDTO[];
  isManual: boolean;
  onAddPosto: () => void;
  onRemovePosto: () => void;
}

export function PostoVisualization({
  postos,
  isManual,
  onAddPosto,
  onRemovePosto,
}: PostoVisualizationProps) {
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