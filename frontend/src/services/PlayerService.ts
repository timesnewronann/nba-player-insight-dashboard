import axios from "axios";
import type { Player } from "../types/Player";
import type { Team } from "../types/Team";
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

export async function getAllTeams(): Promise<Team[]> {
  const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/teams`);
  return response.data;
}

export async function getTeamById(id: number): Promise<Team> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/teams/${id}`,
  );
  return response.data;
}

export async function getPlayersByTeamId(id: number): Promise<Player[]> {
  const response = await axios.get(
    `${import.meta.env.VITE_API_URL}/api/teams/${id}/players`,
  );
  return response.data;
}
