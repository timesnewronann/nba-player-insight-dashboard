import axios from "axios";
import type { Player } from "../types/Player";
import type { SeasonStat } from "../types/SeasonStat";

async function searchPlayers(query: string): Promise<Player[]> {
  const response = await axios.get(
    `http://localhost:8080/api/players/search?query=${query}`,
  );
  return response.data;
}
export default searchPlayers;

async function getPlayerById(id: number): Promise<Player[]> {
  const response = await axios.get(`http://localhost:8000/api/players/${id}`);
  return response.data;
}

async function getSeasonStats(id: number): Promise<SeasonStat> {
  const response = await axios.get(
    `http://localhost:8000/api/players/${id}/season-stats`,
  );
  return response.data;
}

async function getGameLogs(id: number): 