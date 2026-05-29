import type { Player } from "./Player";
import type { Game } from "./Game";

export type ShotChart = {
  id: number;
  player: Player;
  game: Game;
  locX: number;
  locY: number;
  shotMade: boolean;
  shotType: string;
  shotZone: string;
  gameDate: string;
};
