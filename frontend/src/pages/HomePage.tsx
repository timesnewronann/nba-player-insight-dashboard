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
            <h1>READ THE FLOOR</h1>
            <p>Shot charts. Game logs. Season Stats. Every player, Every Game. All in One Place</p>
            <input type="text" 
            placeholder="Search any NBA player..."
            value={searchQuery}
            onChange={(e) => {
                setSearchQuery(e.target.value)
                handleSearch()
            }}
            />
            <button onClick={handleSearch}>Search</button>
            {players.map((player) => (
                <Link to={`/player/${player.id}`} key={player.id}>
                    {player.fullName}
                </Link>
            ))}

        </div>
        )
}

export default HomePage