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
    if (!player) return (
        <div className="flex items-center justify-center min-h-screen">
            <p className="text-fg-muted">Loading...</p>
        </div>
    )

    return (
        <div className="max-w-3xl mx-auto px-4 py-8">
            <div className="bg-fg-bg2 border border-white/10 rounded-lg p-6 mb-4"> {/*Player Header*/}
                <h1 className="font-display text-5xl text-fg-text">{player?.fullName}</h1>
                <h2>Player ID:{player?.id}</h2>
                <ul>
                    <li>Position: {player?.position}</li>
                    <li>Height: {player?.height}</li>
                    <li>Weight: {player?.weight}</li>
                </ul>
            </div>
            <div> {/*Season Stats Grid*/}
            <h2>Season Stats</h2>
                <div> {/*PPG */}
                    <span>{seasonStats[0]?.pointsPerGame}</span>
                    <span>PPG</span>
                </div>
                <div> {/*APG */}
                    <span>{seasonStats[0]?.assistsPerGame}</span>
                    <span>APG</span>
                </div>
                <div> {/*RPG */}
                    <span>{seasonStats[0]?.reboundsPerGame}</span>
                    <span>RPG</span>
                </div>
                <div> {/*FG% */}
                    <span>{((seasonStats[0]?.fieldGoalPct ?? 0) *100).toFixed(2)}</span>
                    <span>FG%</span>
                </div>
            </div>
            <div> {/*Game Logs Table*/}
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
            <div> {/* Shot Chart */}
                <h2>Shot Chart</h2>
            </div>
            
        </div>
    )

}

