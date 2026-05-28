import { useState } from "react";
import { ModeSelection } from "./components/ModeSelection";
import { SimulationDashboard } from "./components/SimulationDashboard";
import type { SimulacaoResponse, SimulacaoModeDTO } from "./types";

function App() {
  const [view, setView] = useState<"selection" | "simulation">("selection");
  const [simulationState, setSimulationState] = useState<SimulacaoResponse | null>(null);
  const [mode, setMode] = useState<SimulacaoModeDTO | null>(null);

  function handleStart(state: SimulacaoResponse, selectedMode: SimulacaoModeDTO) {
    setSimulationState(state);
    setMode(selectedMode);
    setView("simulation");
  }

  if (view === "selection" || !simulationState || !mode) {
    return <ModeSelection onStart={handleStart} />;
  }

  return (
    <SimulationDashboard
      initialState={simulationState}
      mode={mode}
      onReset={() => setView("selection")}
    />
  );
}

export default App;