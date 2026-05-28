import type { SimulacaoModeDTO, SimulacaoResponse } from "./types";

const BASE = "/api/simulacao";

export class ApiError extends Error {
  status: number;
  code: string;
  constructor(status: number, code: string, message: string) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.code = code;
  }
}

async function handleResponse(res: Response): Promise<SimulacaoResponse> {
  if (!res.ok) {
    const data = await res.json().catch(() => ({ erro: "UNKNOWN", mensagem: "Erro desconhecido" }));
    throw new ApiError(res.status, data.erro || "UNKNOWN", data.mensagem || "Erro desconhecido");
  }
  return res.json() as Promise<SimulacaoResponse>;
}

export async function iniciarSimulacao(
  modo: SimulacaoModeDTO
): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/iniciar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ modo }),
  });
  return handleResponse(res);
}

export async function tickSimulacao(): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/tick`, { method: "POST" });
  return handleResponse(res);
}

export async function statusSimulacao(): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/status`, { method: "GET" });
  return handleResponse(res);
}

export async function encerrarSimulacao(): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/encerrar`, { method: "POST" });
  return handleResponse(res);
}

export async function adicionarPosto(): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/posto/adicionar`, { method: "POST" });
  return handleResponse(res);
}

export async function removerPosto(): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/posto/remover`, { method: "POST" });
  return handleResponse(res);
}

export async function desistenciaSenha(
  numeroExibicao: string
): Promise<SimulacaoResponse> {
  const res = await fetch(`${BASE}/senha/${numeroExibicao}/desistencia`, {
    method: "POST",
  });
  return handleResponse(res);
}