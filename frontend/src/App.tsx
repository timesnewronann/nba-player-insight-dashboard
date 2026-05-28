import './App.css'
import {Routes, Route} from 'react-router-dom'
import HomePage from './pages/HomePage'
import PlayerPage from './pages/PlayerPage'
import TeamsPage from './pages/TeamsPage'
import TeamPage from './pages/TeamPage'
import Navbar from './components/Navbar'

function App() {
  return (
    <div className="app min-h-screen bg-gray-950 text-white">
      <Navbar/>
      <div className="pt-20">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/players/:id" element={<PlayerPage/>} />
          <Route path="/teams/" element={<TeamsPage/>} />
          <Route path="/teams/:id" element={<TeamPage/>} />
        </Routes>
      </div>
    </div>
  )
}

export default App