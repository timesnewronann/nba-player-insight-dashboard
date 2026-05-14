function Navbar() {

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 flex items-center justify-between px-8 py-5 bg-fg-bg2 border-b border-white/5">
            <div className="font-display text-2xl tracking-widest cursor-pointer">
                <span className="text-fg-text">FLOOR</span>
                <span className="text-fg-accent">GENERAL</span>
            </div>
            <div className="flex gap-8">
                <a className="text-fg muted hover: text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Players</a>
                <a className="text-fg muted hover: text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Teams</a>
                <a className="text-fg muted hover: text-fg-text text-xs tracking-widest uppercase transition-colors cursor-pointer">Stats</a>
            </div>
            <button>
                Toggle
            </button>
        </nav>
    )
}

export default Navbar

