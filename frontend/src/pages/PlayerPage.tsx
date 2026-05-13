import { useState, useEffect } from "react";
import type { Player } from "../types/Player";
import type { SeasonStat } from "../types/SeasonStat";
import type { GameLog } from "../types/GameLog";
import { useParams } from "react-router-dom";
import { getPlayerById, getGameLogs, getSeasonStats } from "../services/PlayerService";

export default function PlayerPage() {
    const params = useParams();
    const [player, setPlayer] = useState<Player | null>(null);
    const [seasonStats, setSeasonStats] = useState<SeasonStat[]>([]);
    const [gameLogs, setGameLogs]= useState<GameLog[]>([])

    useEffect( () => {
        // fetch code
        const fetchData = async () => {
            // get Player by Id
            const playerData = await getPlayerById(Number(params.id));
            setPlayer(playerData);

            // get Season Stats
            const seasonData = await getSeasonStats(Number(params.id));
            setSeasonStats(seasonData);

            // get game logs
            const gameData = await getGameLogs(Number(params.id));
            setGameLogs(gameData);
            
        }
        fetchData();

    }, []); // <- tells React to run once when component loads

    return (
        <div>
            <h1>{player?.fullName}</h1>
            <h2>Player ID:{player?.id}</h2>
            <ul>
                <li>Position: {player?.position}</li>
                <li>Height: {player?.height}</li>
                <li>Weight: {player?.weight}</li>
            </ul>
            <h2>Season Stats</h2>
                {seasonStats.map((seasonStat) =>(
                    <div key = {seasonStat.id}>
                        <ul>
                            <li>Season: {seasonStat.season}</li>
                            <li>Games Played: {seasonStat?.gamesPlayed}</li>
                            <li>Minutes Per Game: {seasonStat?.minutesPerGame}</li>         
                            <li>Points Per Game: {seasonStat?.pointsPerGame}</li>   
                            <li>Rebounds Per Game: {seasonStat?.reboundsPerGame}</li>
                            <li>Assists Per Game: {seasonStat?.assistsPerGame}</li>
                            <li>Steals Per Game: {seasonStat?.stealsPerGame}</li>
                            <li>Blocks Per Game: {seasonStat?.blocksPerGame}</li>
                            <li>Field Goal Percentage: {((seasonStat.fieldGoalPct ?? 0) *100).toFixed(2)}%</li>
                            <li>Three Point Percentage: {((seasonStat.threePointPct ?? 0) * 100).toFixed(2)}%</li>
                            <li>Free Throw Percentage: {((seasonStat.freeThrowPct  ?? 0)* 100).toFixed(2)}%</li>
                        </ul>
                    
                    </div>
                    
                ))}
            
            <h2>Game Logs</h2>
                {gameLogs.map((gameLog) =>(
                    <div key = {gameLog.id}>
                        <ul>
                            <li>Win/Loss: {gameLog.winLoss}</li>
                            <li>Season: {gameLog.game.season}</li>
                            <li>Game Date: {gameLog.game?.gameDate}</li>
                            <li>Away Team: {gameLog.game?.awayTeam?.abbreviation}</li>
                            <li>Home Team: {gameLog.game?.homeTeam?.abbreviation}</li>
                            <li>Points Scored: {gameLog.ptsScored}</li>
                            <li>Assists: {gameLog.assists}</li>
                            <li>Rebounds: {gameLog.rebounds}</li>
                            <li>Blocks: {gameLog.blocks}</li>
                            <li>Steals: {gameLog.steals}</li>
                            <li>Minutes Player: {gameLog.minutes}</li>
                            <li>Field Goal Percentage: {((gameLog.fieldGoalPct ?? 0) *100).toFixed(2)}%</li>
                            <li>Three Point Percentage: {((gameLog.threePointPct ?? 0) * 100).toFixed(2)}%</li>
                            <li>Free Throw Percentage: {((gameLog.freeThrowPct  ?? 0)* 100).toFixed(2)}%</li>
                            <li>Turnovers: {gameLog.turnovers}</li>
                        </ul>

                    </div>
                ))}
            
        </div>
    )

}

