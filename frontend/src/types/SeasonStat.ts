import type { Player } from "./Player";

export type SeasonStat = {
  id: number;
  player: Player;
  season: string;
  gamesPlayed: number;
  minutesPerGame: bigint;
  reboundsPerGame: bigint;
  assistsPerGame: bigint;
  stealsPerGame: bigint;
  blocksPerGame: bigint;
  fieldGoalPct: bigint;
  threePointPct: bigint;
  freeThrowPct: bigint;
};
