import { useState } from "react"

function HomePage() {
    const [searchQuery, setSearchQuery] = useState('')
    return (
        <div className="HomePage">
            <h1>READ THE FLOOR</h1>
            <h2>Search.</h2>
            <input type="text" 
            placeholder="Search any NBA player..."
            value={searchQuery}
            onChange={(e) =>setSearchQuery(e.target.value)}
            />
            <p>You typed: {searchQuery}</p>
            <p>Shot charts. Game logs. Season Stats. Every player, Every Game. All in One Place</p>
            <h2>Player Season Stats</h2>
            <h2>Player Shot Chart</h2>

        </div>
        )
}

export default HomePage