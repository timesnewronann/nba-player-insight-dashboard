import './App.css'
import {Routes, Route} from 'react-router-dom'
import HomePage from './pages/HomePage'
import PlayerPage from './pages/PlayerPage'
import Navbar from './components/Navbar'

function App() {
  return (
    <div className="app min-h-screen bg-gray-950 text-white">
      <Navbar/>
      <div className="pt-20">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/player/:id" element={<PlayerPage/>} />
        </Routes>
      </div>
    </div>
  )
}

export default App