function Navbar() {

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 flex items-center justify-between px-8 py-5">
            <div className="Navbar">
                <span className="text-fg-text">FLOOR</span>
                <span className="text-fg-accent">GENERAL</span>
            </div>
            <div>
                <a>Players</a>
                <a>Teams</a>
                <a>Stats</a>
            </div>
            <button>
                Toggle
            </button>
        </nav>
    )
}

export default Navbar

