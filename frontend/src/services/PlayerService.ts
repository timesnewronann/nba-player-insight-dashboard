import axios from "axios";
import type { Player } from "../types/Player";
import type { SeasonStat } from "../types/SeasonStat";
import type { GameLog } from "../types/GameLog";

export async function searchPlayers(query: string): Promise<Player[]> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/players/search?query=${query}`,
  );
  return response.data;
}

export async function getPlayerById(id: number): Promise<Player> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/players/${id}`,
  );
  return response.data;
}

export async function getSeasonStats(id: number): Promise<SeasonStat[]> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/players/${id}/season-stats`,
  );
  return response.data;
}

export async function getGameLogs(id: number): Promise<GameLog[]> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/players/${id}/games`,
  );
  return response.data;
}
