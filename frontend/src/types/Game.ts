import type { Team } from "./Team";

export type Game = {
  id: number;
  nbaGameId: number;
  homeTeam: Team | null;
  awayTeam: Team | null;
  homeTeamScore: number | null;
  awayTeamScore: number | null;
  gameDate: string | null;
  season: string;
};
