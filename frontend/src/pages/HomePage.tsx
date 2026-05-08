import { useState } from "react"
import type { Player } from "../types/Player"
import searchPlayers from  "../services/PlayerService"
import { Link } from "react-router-dom"

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
        <div className="HomePage">
            <h1>READ THE FLOOR</h1>
            <h2>Search.</h2>
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
            <p>Shot charts. Game logs. Season Stats. Every player, Every Game. All in One Place</p>
            <h2>Player Season Stats</h2>
            <h2>Player Shot Chart</h2>

        </div>
        )
}

export default HomePage