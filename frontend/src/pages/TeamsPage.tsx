import { useEffect, useState } from "react";
import type { Team } from "../types/Team";
import { getAllTeams } from "../services/PlayerService";
import { Link } from "react-router-dom";
export default function TeamsPage() {
    const [teams, setTeams] = useState<Team[]>([]);

    useEffect( () => {
        // fetch code
        const fetchData = async () => {
            // get all teams
            const teamsData = await getAllTeams();
            setTeams(teamsData);
            
        }
        fetchData();
    }, []);
    if (teams.length === 0) return (
        <div>
            <p>Loading...</p>
        </div>
    )

    const eastTeams = teams.filter(team => team.conference === 'East')
    const westTeams = teams.filter(team => team.conference === 'West')
    return (
    <div className="max-w-5xl mx-auto px-4 py-8">
        <h1 className="font-display text-5xl text-fg-text mb-6">Teams</h1>
        <div className="grid grid-cols-2 gap-8 mt-6">
            {/* East Column */}
            <div className="bg-fg-bg2 border border-white/10 rounded-lg overflow-hidden">
                <h2 className="font-mono text-xs text-fg-muted uppercase tracking-widest p-4 border-b border-white/10">Eastern Conference</h2>
                {eastTeams.map((team) => (
                    <div className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 transition-colors">
                        <Link to={`/teams/${team.id}`} key={team.id} className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 transition-colors">
                            {team.teamName}
                        </Link>
                    </div>
                ))}
            </div>
            <div className="bg-fg-bg2 border border-white/10 rounded-lg overflow-hidden">
            {/* West Column */}
                <h2 className="font-mono text-xs text-fg-muted uppercase tracking-widest p-4 border-b border-white/10">Western Conference</h2>
                {westTeams.map((team) => (
                    <div className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 transition-colors">
                    <Link to={`/teams/${team.id}`} key={team.id} className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 transition-colors">
                        {team.teamName}
                    </Link>
                    </div>
                ))}
            </div>
        </div>
    </div>
    )
}
