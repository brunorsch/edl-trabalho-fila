import {
  Ticket,
  CheckCircle2,
  XCircle,
  Monitor,
  MonitorOff,
  Trophy,
} from "lucide-react";
import type { EventoCicloDTO } from "../types";

interface EventLogProps {
  eventos: EventoCicloDTO[];
}

function getEventStyle(tipo: string): {
  icon: React.ReactNode;
  textColor: string;
} {
  if (tipo.includes("GERADA") || tipo.includes("GERACAO"))
    return {
      icon: <Ticket size={14} className="text-blue-400" />,
      textColor: "text-blue-300",
    };
  if (tipo.includes("ATENDIDA") || tipo.includes("ATENDIMENTO"))
    return {
      icon: <CheckCircle2 size={14} className="text-emerald-400" />,
      textColor: "text-emerald-300",
    };
  if (tipo.includes("DESISTENCIA"))
    return {
      icon: <XCircle size={14} className="text-red-400" />,
      textColor: "text-red-300",
    };
  if (tipo.includes("ATIVADO"))
    return {
      icon: <Monitor size={14} className="text-purple-400" />,
      textColor: "text-purple-300",
    };
  if (tipo.includes("DESATIVADO"))
    return {
      icon: <MonitorOff size={14} className="text-orange-400" />,
      textColor: "text-orange-300",
    };
  if (tipo.includes("FINALIZADA"))
    return {
      icon: <Trophy size={14} className="text-yellow-400" />,
      textColor: "text-yellow-300",
    };
  return {
    icon: <Ticket size={14} className="text-white/50" />,
    textColor: "text-white/50",
  };
}

export function LogEventos({ eventos }: EventLogProps) {
  const reversed = [...eventos].reverse();

  return (
    <div className="rounded-xl bg-white/5 border border-white/10 p-4 flex-1 overflow-hidden flex flex-col">
      <h3 className="text-xs font-semibold uppercase tracking-wider text-white/50 mb-3">
        Últimos Eventos
      </h3>
      <div className="flex-1 overflow-y-auto space-y-2 text-sm">
        {reversed.length === 0 ? (
          <p className="text-white/30 text-xs">Nenhum evento neste tick</p>
        ) : (
          reversed.map((evento, i) => {
            const style = getEventStyle(evento.tipo);
            return (
              <div
                key={i}
                className="flex items-start gap-2 py-1.5 border-b border-white/5 last:border-0"
              >
                {style.icon}
                <div className="min-w-0">
                  <p
                    className={`text-xs leading-snug ${style.textColor}`}
                  >
                    {evento.descricao}
                  </p>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}