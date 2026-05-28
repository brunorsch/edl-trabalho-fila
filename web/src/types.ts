export interface SenhaDTO {
  numero: number;
  tipo: "PRIORITARIA" | "NORMAL";
  status: "AGUARDANDO" | "EM_ATENDIMENTO" | "FINALIZADO" | "DESISTENCIA";
  numeroExibicao: string;
  ehPrioritario: boolean;
}

export interface PostoDTO {
  slot: number;
  emAtendimento: boolean;
  senha: SenhaDTO | null;
}

export interface EventoCicloDTO {
  tipo: string;
  descricao: string;
  senha: SenhaDTO | null;
  posto: PostoDTO | null;
}

export interface SimulacaoResponse {
  tickAtual: number;
  normal: SenhaDTO[];
  prioritaria: SenhaDTO[];
  proximasDuas: SenhaDTO[];
  postos: PostoDTO[];
  atendidas: SenhaDTO[];
  totalAtendidas: number;
  totalGeradas: number;
  totalDesistencias: number;
  eventosUltimoCiclo: EventoCicloDTO[];
  encerrada: boolean;
  modo: "AUTO" | "MANUAL";
}

export type SimulacaoModeDTO = "AUTO" | "MANUAL";