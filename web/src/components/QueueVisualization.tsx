import { Users, Hand } from "lucide-react";
import type { SenhaDTO } from "../types";

interface QueueVisualizationProps {
  normal: SenhaDTO[];
  prioritaria: SenhaDTO[];
  proximasDuas: SenhaDTO[];
  isManual: boolean;
  onDesistencia: (numeroExibicao: string) => void;
}

function SenhaBadge({
  senha,
  onClick,
  clickable,
}: {
  senha: SenhaDTO;
  onClick?: () => void;
  clickable?: boolean;
}) {
  const isPrioritaria = senha.ehPrioritario;
  const baseClasses = isPrioritaria
    ? "bg-purple-500/20 border-purple-500/40 text-purple-200"
    : "bg-blue-500/20 border-blue-500/40 text-blue-200";

  return (
    <button
      onClick={onClick}
      disabled={!clickable}
      className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border text-sm font-mono font-medium transition-colors ${baseClasses} ${clickable ? "cursor-pointer hover:opacity-80" : "cursor-default"}`}
    >
      {senha.numeroExibicao}
      {senha.status === "EM_ATENDIMENTO" && (
        <span className="text-[10px] uppercase tracking-wider opacity-70">
          atendendo
        </span>
      )}
    </button>
  );
}

export function QueueVisualization({
  normal,
  prioritaria,
  proximasDuas,
  isManual,
  onDesistencia,
}: QueueVisualizationProps) {
  const aguardandoNormal = normal.filter((s) => s.status === "AGUARDANDO");
  const aguardandoPrioritaria = prioritaria.filter(
    (s) => s.status === "AGUARDANDO"
  );

  function handleDesistenciaClick(numeroExibicao: string) {
    if (window.confirm("Desistir da fila?")) {
      onDesistencia(numeroExibicao);
    }
  }

  return (
    <section className="lg:col-span-5 flex flex-col gap-4">
      {/* Próximas highlight */}
      {proximasDuas.length > 0 && (
        <div className="rounded-xl bg-amber-500/10 border border-amber-500/30 p-4">
          <h3 className="text-xs font-semibold uppercase tracking-wider text-amber-300 mb-2">
            Próximas a serem chamadas
          </h3>
          <div className="flex flex-wrap gap-2">
            {proximasDuas.map((s) => (
              <SenhaBadge key={s.numeroExibicao} senha={s} />
            ))}
          </div>
        </div>
      )}

      {/* Fila Normal */}
      <div className="rounded-xl bg-blue-500/5 border border-blue-500/20 p-4 flex-1">
        <div className="flex items-center gap-2 mb-3">
          <Users size={16} className="text-blue-400" />
          <h2 className="text-sm font-semibold uppercase tracking-wider text-blue-300">
            Fila Normal
          </h2>
          <span className="ml-auto text-xs text-white/40 font-mono">
            {aguardandoNormal.length}{" "}
            {aguardandoNormal.length === 1 ? "senha" : "senhas"}
          </span>
        </div>
        <div className="flex flex-wrap gap-2">
          {aguardandoNormal.length === 0 ? (
            <p className="text-white/30 text-sm">Nenhuma senha na fila</p>
          ) : (
            aguardandoNormal.map((s) => (
              <SenhaBadge
                key={s.numeroExibicao}
                senha={s}
                clickable={isManual}
                onClick={isManual ? () => handleDesistenciaClick(s.numeroExibicao) : undefined}
              />
            ))
          )}
        </div>
      </div>

      {/* Fila Prioritária */}
      <div className="rounded-xl bg-purple-500/5 border border-purple-500/20 p-4 flex-1">
        <div className="flex items-center gap-2 mb-3">
          <Hand size={16} className="text-purple-400" />
          <h2 className="text-sm font-semibold uppercase tracking-wider text-purple-300">
            Fila Prioritária
          </h2>
          <span className="ml-auto text-xs text-white/40 font-mono">
            {aguardandoPrioritaria.length}{" "}
            {aguardandoPrioritaria.length === 1 ? "senha" : "senhas"}
          </span>
        </div>
        <div className="flex flex-wrap gap-2">
          {aguardandoPrioritaria.length === 0 ? (
            <p className="text-white/30 text-sm">Nenhuma senha na fila</p>
          ) : (
            aguardandoPrioritaria.map((s) => (
              <SenhaBadge
                key={s.numeroExibicao}
                senha={s}
                clickable={isManual}
                onClick={isManual ? () => handleDesistenciaClick(s.numeroExibicao) : undefined}
              />
            ))
          )}
        </div>
      </div>
    </section>
  );
}