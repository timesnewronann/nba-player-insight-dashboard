import { useEffect, useState } from "react";
import { getPlayersByTeamId, getTeamById } from "../services/PlayerService";
import { Link, useParams } from "react-router-dom";
import type { Team } from "../types/Team";
import type { Player } from "../types/Player";

export default function TeamPage() {
    const params = useParams();
    const [team, setTeam] = useState<Team | null>(null);
    const [players, setPlayers] = useState <Player[]>([]);

    
    useEffect( () => {
        // fetch code
        const fetchData = async () => {
            // get Team by Id
            const teamData = await getTeamById(Number(params.id));
            setTeam(teamData);

            // get players by TeamId
            const playersData = await getPlayersByTeamId(Number(params.id))
            setPlayers(playersData);
        }
        fetchData();
    }, []);
    if (!team) return (
        <div>
            Loading...
        </div>
    )
    
    return (
        <div>
            <h1>{team.teamName}</h1>
            <h2>{team.city}</h2>
            <h2>{team.conference}</h2>
            <h2>{team.division}</h2>

            <h1>Roster</h1>
            {players.map((player) => (
                <Link to={`/teams/${team.id}/players`} key={team.id}>
                    
                    <Link to={`/players/${player.id}`}>{player.fullName} - {player.position}</Link>
                </Link>
            ))}
        </div>
    )
}