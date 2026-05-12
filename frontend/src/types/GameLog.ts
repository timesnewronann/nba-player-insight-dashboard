import type { Game } from "./Game";
import type { Player } from "./Player";
import type { Team } from "./Team";

export type GameLog = {
  id: number;
  player: Player;
  game: Game;
  team: Team;
  ptsScored: number;
  assists: number;
  rebounds: number;
  blocks: number;
  steals: number;
  minutes: number;
  fieldGoalPct: number;
  threePointPct: number;
  freeThrowPct: number;
  turnovers: number;
  winLoss: string | null;
};
