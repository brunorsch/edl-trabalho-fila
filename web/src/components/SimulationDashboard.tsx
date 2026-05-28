import {
  Play,
  Pause,
  ArrowRight,
  Square,
  AlertCircle,
  LogOut,
} from "lucide-react";
import { useSimulation } from "../hooks/useSimulation";
import type { SimulacaoResponse, SimulacaoModeDTO } from "../types";
import { FilaVisualization } from "./FilaVisualization.tsx";
import { PostoVisualization } from "./PostoVisualization";
import { Stats } from "./Stats.tsx";
import { LogEventos } from "./LogEventos.tsx";

interface SimulationDashboardProps {
  initialState: SimulacaoResponse;
  mode: SimulacaoModeDTO;
  onReset: () => void;
}

export function SimulationDashboard({
  initialState,
  mode,
  onReset,
}: SimulationDashboardProps) {
  const {
    state,
    isPaused,
    isLoading,
    error,
    countdown,
    tickNotification,
    tick,
    encerrar,
    pause,
    resume,
    addPosto,
    removePosto,
    desistencia,
  } = useSimulation(initialState, mode);

  return (
    <div className="min-h-screen bg-slate-900 text-white flex flex-col">
      {/* Notifications */}
      {error && (
        <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 flex items-center gap-2 px-4 py-3 rounded-xl bg-red-500/20 border border-red-500/40 text-red-200 text-sm shadow-lg backdrop-blur-sm">
          <AlertCircle size={16} />
          {error}
        </div>
      )}
      {tickNotification && (
        <div className="fixed top-4 right-4 z-50 flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-500/20 border border-emerald-500/40 text-emerald-200 text-sm shadow-lg backdrop-blur-sm animate-pulse">
          <Play size={14} />
          {tickNotification}
        </div>
      )}

      {/* Header */}
      <header className="border-b border-white/10 bg-slate-800/50 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4 flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-4">
            <h1 className="text-xl font-bold tracking-tight">
              Simulação de Filas
            </h1>
            <span
              className={`px-2.5 py-0.5 rounded-full text-xs font-semibold uppercase tracking-wider ${
                mode === "AUTO"
                  ? "bg-emerald-500/20 text-emerald-300 border border-emerald-500/30"
                  : "bg-blue-500/20 text-blue-300 border border-blue-500/30"
              }`}
            >
              {mode === "AUTO" ? "Automático" : "Manual"}
            </span>
            <span className="text-sm text-white/50 font-mono">
              Tick #{state.tickAtual}
            </span>
            {mode === "AUTO" && !state.encerrada && (
              <div className="flex items-center gap-2">
                <div className="w-24 h-1.5 rounded-full bg-white/10 overflow-hidden">
                  <div
                    className="h-full rounded-full bg-emerald-400 transition-all duration-1000 ease-linear"
                    style={{ width: `${(countdown / 10) * 100}%` }}
                  />
                </div>
                <span className="text-xs text-emerald-300/70 font-mono w-8">
                  {isPaused ? "Pausado" : `${countdown}s`}
                </span>
              </div>
            )}
          </div>

          <div className="flex items-center gap-2">
            {mode === "AUTO" && !state.encerrada && (
              <button
                onClick={isPaused ? resume : pause}
                disabled={isLoading}
                className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-white/10 hover:bg-white/15 text-white/80 text-sm transition-colors disabled:opacity-50"
              >
                {isPaused ? <Play size={14} /> : <Pause size={14} />}
                {isPaused ? "Retomar" : "Pausar"}
              </button>
            )}

            {mode === "MANUAL" && !state.encerrada && (
              <button
                onClick={tick}
                disabled={isLoading}
                className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium transition-colors disabled:opacity-50"
              >
                <ArrowRight size={14} />
                Avançar Tick
              </button>
            )}

            {!state.encerrada ? (
              <button
                onClick={encerrar}
                disabled={isLoading}
                className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-red-600/80 hover:bg-red-500 text-white text-sm font-medium transition-colors disabled:opacity-50"
              >
                <Square size={14} />
                Encerrar
              </button>
            ) : (
              <span className="px-3 py-2 rounded-lg bg-red-500/20 border border-red-500/30 text-red-300 text-sm font-medium">
                Encerrada
              </span>
            )}

            <button
              onClick={onReset}
              className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-white/5 hover:bg-white/10 text-white/50 text-sm transition-colors"
            >
              <LogOut size={14} />
              Sair
            </button>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="flex-1 max-w-7xl mx-auto w-full px-4 sm:px-6 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 h-full">
          <FilaVisualization
            normal={state.normal}
            prioritaria={state.prioritaria}
            proximasDuas={state.proximasDuas}
            isManual={mode === "MANUAL" && !state.encerrada}
            onDesistencia={desistencia}
          />

          <PostoVisualization
            postos={state.postos}
            atendidas={state.atendidas}
            isManual={mode === "MANUAL" && !state.encerrada}
            encerrada={state.encerrada}
            onAddPosto={addPosto}
            onRemovePosto={removePosto}
          />

          <section className="lg:col-span-3 flex flex-col gap-4">
            <Stats
              totalGeradas={state.totalGeradas}
              totalAtendidas={state.totalAtendidas}
              totalDesistencias={state.totalDesistencias}
              tickAtual={state.tickAtual}
            />
            <LogEventos eventos={state.eventosUltimoCiclo} />
          </section>
        </div>
      </main>
    </div>
  );
}