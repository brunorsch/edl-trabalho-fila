import { useState, useEffect, useCallback, useRef } from "react";
import {
  tickSimulacao,
  encerrarSimulacao,
  adicionarPosto,
  removerPosto,
  desistenciaSenha,
} from "../api";
import type { SimulacaoResponse, SimulacaoModeDTO } from "../types";

export function useSimulation(initialState: SimulacaoResponse, mode: SimulacaoModeDTO) {
  const [state, setState] = useState<SimulacaoResponse>(initialState);
  const [isPaused, setIsPaused] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const handleError = (err: unknown) => {
    const message = err instanceof Error ? err.message : "Erro na simulação";
    setError(message);
    setTimeout(() => setError(null), 5000);
  };

  const tick = useCallback(async () => {
    if (isLoading || state.encerrada) return;
    setIsLoading(true);
    try {
      const newState = await tickSimulacao();
      setState(newState);
    } catch (err) {
      handleError(err);
    } finally {
      setIsLoading(false);
    }
  }, [isLoading, state.encerrada]);

  const encerrar = useCallback(async () => {
    setIsLoading(true);
    try {
      const newState = await encerrarSimulacao();
      setState(newState);
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
    } catch (err) {
      handleError(err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const pause = useCallback(() => {
    setIsPaused(true);
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  }, []);

  const resume = useCallback(() => {
    setIsPaused(false);
  }, []);

  const addPosto = useCallback(async () => {
    setIsLoading(true);
    try {
      const newState = await adicionarPosto();
      setState(newState);
    } catch (err) {
      handleError(err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const removePosto = useCallback(async () => {
    setIsLoading(true);
    try {
      const newState = await removerPosto();
      setState(newState);
    } catch (err) {
      handleError(err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const desistencia = useCallback(async (numeroExibicao: string) => {
    setIsLoading(true);
    try {
      const newState = await desistenciaSenha(numeroExibicao);
      setState(newState);
    } catch (err) {
      handleError(err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  // Auto-tick every 10 seconds in AUTO mode
  useEffect(() => {
    if (mode === "AUTO" && !isPaused && !state.encerrada) {
      intervalRef.current = setInterval(() => {
        tickSimulacao().then(setState).catch(handleError);
      }, 10000);
    }
    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
    };
  }, [mode, isPaused, state.encerrada]);

  return {
    state,
    isPaused,
    isLoading,
    error,
    tick,
    encerrar,
    pause,
    resume,
    addPosto,
    removePosto,
    desistencia,
  };
}