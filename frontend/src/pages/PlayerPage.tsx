import { useState, useEffect } from "react";
import type { Player } from "../types/Player";
import type { SeasonStat } from "../types/SeasonStat";
import type { GameLog } from "../types/GameLog";
import type { ShotChart } from "../types/ShotChart";
import { useParams } from "react-router-dom";
import { getPlayerById, getGameLogs, getSeasonStats, getShotsByPlayerId } from "../services/PlayerService";
import ShotChartComponent from "../components/ShotChartComponent";

export default function PlayerPage() {
    const params = useParams();
    const [player, setPlayer] = useState<Player | null>(null);
    const [seasonStats, setSeasonStats] = useState<SeasonStat[]>([]);
    const [shotChart, setShotChart] = useState<ShotChart[]>([]);
    const [gameLogs, setGameLogs]= useState<GameLog[]>([])
    const [isModalOpen, setIsModalOpen] = useState(false)

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

            // get shot chart
            const shotData = await getShotsByPlayerId(Number(params.id));
            setShotChart(shotData);
            
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
                <div className="flex items-center gap-6">
                    <img
                        src={`https://cdn.nba.com/headshots/nba/latest/1040x760/${player.nbaPlayerId}.png`}
                        alt={player.fullName}
                        className="w-20 h-20 rounded-full object-cover cursor-pointer"
                        onClick={() => setIsModalOpen(true)}
                        onError={(e) => {
                            e.currentTarget.style.display = 'none'
                        }}
                    />
                    <h1 className="font-display text-5xl text-fg-text">{player?.fullName}</h1>
                </div>
                <div className="flex gap-4 mt-2 text-sm text-fg-muted">
                    <span>{player?.position}</span>
                    <span>{player?.active ? 'Active' : 'Inactive'}</span>
                </div>
            </div>
            {isModalOpen && (
                <div
                    className="fixed inset-0 bg-black/80 flex items-center justify-center z-50 cursor-pointer"
                    onClick={() => setIsModalOpen(false)}
                >
                    <img 
                    src={`https://cdn.nba.com/headshots/nba/latest/1040x760/${player.nbaPlayerId}.png`}
                    alt={player.fullName}
                    className="max-w-sm rounded-lg"
                    />
                </div>
            )}
            <h2 className="font-mono text-xl text-fg-text">
                {seasonStats[0]?.season} Season Stats
                </h2>
            <div className="bg-fg-bg2 border border-white/10 rounded-lg mb-4 grid grid-cols-4"> {/*Season Stats Grid*/}
                <div className="p-6 border-r border-white/10 text-center"> {/*PPG */}
                    <span className="block font-mono text-3xl text-fg-text">{seasonStats[0]?.pointsPerGame}</span>
                    <span className="block text-xs text-fg-muted uppercase tracking-widest mt-1">PPG</span>
                </div>
                <div className="p-6 border-r border-white/10 text-center"> {/*APG */}
                    <span className="block font-mono text-3xl text-fg-text">{seasonStats[0]?.assistsPerGame}</span>
                    <span className="block text-xs text-fg-muted uppercase tracking widest mt-1">APG</span>
                </div>
                <div className="p-6 border-r border-white/10 text-center"> {/*RPG */}
                    <span className="block font-mono text-3xl text-fg-text">{seasonStats[0]?.reboundsPerGame}</span>
                    <span className="block text-xs text-fg-muted uppercase tracking widest mt-1">RPG</span>
                </div>
                <div className="p-6 text-center"> {/*FG% */}
                    <span className="block font-mono text-3xl text-fg-text">{((seasonStats[0]?.fieldGoalPct ?? 0) *100).toFixed(2)}</span>
                    <span className="block text-xs text-fg-muted uppercase tracking-widest mt-1">FG%</span>
                </div>
            </div>

            <h2 className="font-mono text-xl text-fg-text uppercase tracking-widest mb-4">Game Logs</h2>
            <div className="bg-fg-bg2 border border-white/10 rounded-lg p-6"> {/*Game Logs Table*/}
                {gameLogs.map((gameLog) =>(
                    <div key = {gameLog.id} className="flex items-center gap-4 py-3 border-b border-white/5">
                        {/* Win/Loss Badge */}
                        <div className={`w-7 h-7 rounded flex items-center justify-center text-xs font-bold flex-shrink-0 ${gameLog.winLoss === 'W' ? 'bg-green-500/20 text-green-400' : 'bg-red-500/20 text-red-400'}`}>
                            {gameLog.winLoss}
                        </div>
                        {/* Opponent label */}
                        <span className="text-fg-muted text-sm flex-1">
                            {gameLog.game?.gameDate
                                ? new Date(gameLog.game.gameDate + "T12:00:00").toLocaleDateString('en-US', {month: 'short', day: 'numeric', year: 'numeric'})
                                : gameLog.game.season}
                        </span>

                        {/* Stats */}
                        <span className="font-mono text-fg-text">{gameLog.ptsScored}</span>
                        <span className="text-fg-muted text-xs w-6">PTS</span>
                        <span className="font-mono text-fg-text">{gameLog.rebounds}</span>
                        <span className="text-fg-muted text-xs w-6">REB</span>
                        <span className="font-mono text-fg-text">{gameLog.assists}</span>
                        <span className="text-fg-muted text-xs w-6">AST</span>

                        {/* FG% - green above 50%, red below 50% */}
                        <span className={`font-mono text-sm ${(gameLog.fieldGoalPct ?? 0) >= (seasonStats[0]?.fieldGoalPct ?? 0) ? 'text-green-400' : 'text-red-400'}`}>
                            {((gameLog.fieldGoalPct ?? 0) * 100).toFixed(0)}%
                        </span>
                        {/* Potentially Keep these for advanced indepth stats when we click on a specific game to get more details */}
                        {/* <ul> 
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
                        </ul> */}

                    </div>
                ))}
            </div>
            <div> {/* Shot Chart */}
                <h2>Shot Chart</h2>
                <ShotChartComponent shots={shotChart} />
            </div>
            
        </div>
    )

}

