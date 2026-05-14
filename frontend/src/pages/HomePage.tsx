import { useState } from "react"
import type { Player } from "../types/Player"
import { Link } from "react-router-dom"
import { searchPlayers } from "../services/PlayerService"

function HomePage() {
    const [searchQuery, setSearchQuery] = useState('')
    const [players, setPlayers] = useState<Player[]>([])
    const handleSearch = async () => {
        // call searchPlayers with searchQuery
        const searchResult = await searchPlayers(searchQuery)
        // update players state with the results
        setPlayers(searchResult)
    }
    

    return (
        <div className="flex flex-col items-center justify-center min-h-screen">
            <h1 className="font-display text-8xl text-fg-text tracking-wide text-center">READ THE FLOOR</h1>
            <p className="text-fg-muted text-center max-w-md mt-4">Shot charts. Game logs. Season Stats. Every player, Every Game. All in One Place</p>
            <div className="relative w-full max-w-md mt-8">
                <input className="w-full max-w-md bg-fg-bg3 border border-white/10 rounded px-4 py-3 text-fg-text placeholder-fg-muted outline-none focus:border-fg-accent mt-8" type="text" 
                placeholder="Search any NBA player..."
                value={searchQuery}
                onChange={(e) => {
                    setSearchQuery(e.target.value)
                    handleSearch()
                }}
                />
                <button className="mt-3 px-8 bg-fg-aceent text-white font-semibold rounded hover:bg-orange-600 transition-colors" onClick={handleSearch}>Search</button>
            </div>
            {players.length > 0 && (
                <div className="w-full max-w-md bg-fg-bg3 border border-white/10 rounded mt-1">
                    {players.map((player) => (
                        <Link to={`/player/${player.id}`}
                         key={player.id}
                         className="block px-4 py-3 text-fg-text hover:bg-fg-accent/10 hover:text-fg-accent border-b border-white/5 last:border-0 transition-colors"
                        >
                            {player.fullName}
                        </Link>
                    ))}
                </div>
            )}

        </div>
        )
}

export default HomePage