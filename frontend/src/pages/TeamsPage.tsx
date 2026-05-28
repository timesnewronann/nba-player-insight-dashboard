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
    <div>
        <h1>Teams</h1>
        <h2>Eastern Conference</h2>
        {eastTeams.map((team) => (
            <Link to={`/teams/${team.id}`} key={team.id}>
                {team.teamName}
            </Link>
        ))}
        <h2>Western Conference</h2>
        {westTeams.map((team) => (
            <Link to={`/teams/${team.id}`} key={team.id}>
                {team.teamName}
            </Link>
        ))}
    </div>
    )
}
