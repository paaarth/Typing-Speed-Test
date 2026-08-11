import { useEffect, useState, useMemo } from 'react';
import { fetchStats, fetchHistory } from '../../api/resultApi.js';
import { useAuth } from '../../context/AuthContext.jsx';
import StatsCards from './StatsCards.jsx';
import SpeedGraph from './SpeedGraph.jsx';
import HistoryTable from './HistoryTable.jsx';
import './Profile.css';

const FILTERS = ['ALL', 'EASY', 'MEDIUM', 'HARD'];

export default function Profile() {
  const { username } = useAuth();
  const [stats, setStats] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    Promise.all([fetchStats(), fetchHistory()])
      .then(([statsData, historyData]) => {
        setStats(statsData);
        setHistory(historyData);
      })
      .catch(() => setError('Could not load your profile. Is the backend running on port 8080?'))
      .finally(() => setLoading(false));
  }, []);

  const filteredHistory = useMemo(() => {
    if (filter === 'ALL') return history;
    return history.filter((r) => r.difficulty === filter);
  }, [history, filter]);

  const chronological = useMemo(() => [...filteredHistory].reverse(), [filteredHistory]);

  return (
    <div className="container">
      <h1 className="page-title">Your progress</h1>
      <p className="page-subtitle">Signed in as {username}</p>

      {loading && <p className="loading-text">Loading your stats…</p>}
      {error && <p className="error-banner">{error}</p>}

      {!loading && !error && stats && (
        <>
          <StatsCards stats={stats} />

          <div className="card profile-section">
            <div className="profile-section-header">
              <h2 className="section-title">Speed over time</h2>
              <div className="filter-row">
                {FILTERS.map((f) => (
                  <button
                    key={f}
                    type="button"
                    className={`keycap filter-btn ${f === filter ? 'keycap-blueberry' : 'keycap-ghost'}`}
                    onClick={() => setFilter(f)}
                  >
                    {f === 'ALL' ? 'All' : f.charAt(0) + f.slice(1).toLowerCase()}
                  </button>
                ))}
              </div>
            </div>
            <SpeedGraph data={chronological} />
          </div>

          <div className="card profile-section">
            <h2 className="section-title">Recent tests</h2>
            <HistoryTable results={filteredHistory} />
          </div>
        </>
      )}
    </div>
  );
}
