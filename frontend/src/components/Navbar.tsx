import { Link } from "react-router-dom"
function Navbar() {

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 flex items-center justify-between px-8 py-5 bg-fg-bg2 border-b border-white/5">
            <div className="font-display text-2xl tracking-widest cursor-pointer">
                <Link to="/" className="text-fg-text">FLOOR</Link>
                <Link to="/" className="text-fg-accent">GENERAL</Link>
            </div>
            <div className="flex gap-8">
                <Link to="/" className="text-fg-muted hover:text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Players</Link>
                <Link to="/teams" className="text-fg-muted hover:text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Teams</Link>
                <Link to="/" className="text-fg-muted hover:text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Stats</Link>
            </div>
            <button>
                Toggle
            </button>
        </nav>
    )
}

export default Navbar

