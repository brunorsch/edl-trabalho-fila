import { useState } from "react";
import { Play, Hand } from "lucide-react";
import { iniciarSimulacao } from "../api";
import type { SimulacaoResponse, SimulacaoModeDTO } from "../types";

interface ModeSelectionProps {
  onStart: (state: SimulacaoResponse, mode: SimulacaoModeDTO) => void;
}

export function SeletorModo({ onStart }: ModeSelectionProps) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSelect(mode: SimulacaoModeDTO) {
    setLoading(true);
    setError(null);
    try {
      const state = await iniciarSimulacao(mode);
      onStart(state, mode);
    } catch (err: any) {
      setError(err.message || "Erro ao iniciar simulação");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      <div className="backdrop-blur-md bg-white/10 border border-white/20 rounded-2xl p-10 shadow-2xl max-w-md w-full mx-4">
        <h1 className="text-3xl font-bold text-white text-center mb-2">Simulação de Filas</h1>
        <p className="text-white/70 text-center mb-8">Escolha o modo de simulação</p>

        <div className="flex flex-col gap-4">
          <button
            onClick={() => handleSelect("AUTO")}
            disabled={loading}
            className="flex items-center justify-center gap-3 px-6 py-4 rounded-xl bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 text-white font-semibold text-lg transition-colors"
          >
            <Play size={24} />
            Modo Automático
          </button>

          <button
            onClick={() => handleSelect("MANUAL")}
            disabled={loading}
            className="flex items-center justify-center gap-3 px-6 py-4 rounded-xl bg-blue-600 hover:bg-blue-500 disabled:opacity-50 text-white font-semibold text-lg transition-colors"
          >
            <Hand size={24} />
            Modo Manual
          </button>
        </div>

        {error && (
          <div className="mt-4 p-3 rounded-lg bg-red-500/20 border border-red-500/30 text-red-200 text-sm text-center">
            {error}
          </div>
        )}

        {loading && (
          <div className="mt-4 text-center text-white/60 text-sm">Iniciando simulação...</div>
        )}
      </div>
    </div>
  );
}