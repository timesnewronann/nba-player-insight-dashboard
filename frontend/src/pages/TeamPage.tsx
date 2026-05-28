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
        <div className="max-w-3xl mx-auto px-4 py-8"> {/* Outer Container */}
            <div className="bg-fg-bg2 border border-white/10 rounded-lg p-6 mb-4"> {/* Team Header */}
                <h1 className="font-display text-5xl text-fg-text">{team.teamName}</h1>
                <div className="flex gap-4 mt-2 text-sm text-fg-muted">
                    <span>{team.city}</span>
                    <span>{team.conference}</span>
                    <span>{team.division}</span>
                </div>
            </div>


            <h1 className="font-mono text-xl text-fg-text uppercase tracking-widest mb-4">Roster</h1>
            <div className="bg-fg-bg2 border border-white/10 rounded-lg overflow-hidden">
                {players.map((player) => (
                    <Link to={`/players/${player.id}`} key={player.id} className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 transition-colors">
                        {player.fullName} - {player.position}
                    </Link>
                ))}
            </div>
        </div>
    )
}