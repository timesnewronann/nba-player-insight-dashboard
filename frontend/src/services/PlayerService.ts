import axios from "axios";
import type { Player } from "../types/Player";

async function searchPlayers(query: string): Promise<Player[]> {
  const response = await axios.get(
    `http://localhost:8080/api/players/search?query=${query}`,
  );
  return response.data;
}
export default searchPlayers;
