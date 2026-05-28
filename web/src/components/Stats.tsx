import { Ticket, CheckCircle2, XCircle, Clock } from "lucide-react";

interface StatisticsProps {
  totalGeradas: number;
  totalAtendidas: number;
  totalDesistencias: number;
  tickAtual: number;
}

export function Stats({
  totalGeradas,
  totalAtendidas,
  totalDesistencias,
  tickAtual,
}: StatisticsProps) {
  return (
    <div className="grid grid-cols-2 lg:grid-cols-1 gap-3">
      <div className="rounded-xl bg-white/5 border border-white/10 p-3">
        <div className="flex items-center gap-1.5 text-xs text-white/40 uppercase tracking-wider">
          <Ticket size={12} />
          Geradas
        </div>
        <div className="text-2xl font-bold font-mono mt-1">{totalGeradas}</div>
      </div>
      <div className="rounded-xl bg-white/5 border border-white/10 p-3">
        <div className="flex items-center gap-1.5 text-xs text-white/40 uppercase tracking-wider">
          <CheckCircle2 size={12} />
          Atendidas
        </div>
        <div className="text-2xl font-bold font-mono mt-1 text-emerald-400">
          {totalAtendidas}
        </div>
      </div>
      <div className="rounded-xl bg-white/5 border border-white/10 p-3">
        <div className="flex items-center gap-1.5 text-xs text-white/40 uppercase tracking-wider">
          <XCircle size={12} />
          Desistências
        </div>
        <div className="text-2xl font-bold font-mono mt-1 text-red-400">
          {totalDesistencias}
        </div>
      </div>
      <div className="rounded-xl bg-white/5 border border-white/10 p-3">
        <div className="flex items-center gap-1.5 text-xs text-white/40 uppercase tracking-wider">
          <Clock size={12} />
          Tick Atual
        </div>
        <div className="text-2xl font-bold font-mono mt-1">{tickAtual}</div>
      </div>
    </div>
  );
}