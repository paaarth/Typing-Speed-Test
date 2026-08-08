const CARDS = [
  { key: 'bestWpm', label: 'Best WPM', accent: 'strawberry' },
  { key: 'averageWpm', label: 'Average WPM', accent: 'mint' },
  { key: 'averageAccuracy', label: 'Avg Accuracy', accent: 'blueberry', suffix: '%' },
  { key: 'totalTests', label: 'Tests Taken', accent: 'banana' },
];

export default function StatsCards({ stats }) {
  return (
    <div className="stats-cards">
      {CARDS.map((c) => (
        <div key={c.key} className={`card stat-tile accent-${c.accent}`}>
          <span className="stat-tile-value">
            {stats[c.key]}
            {c.suffix || ''}
          </span>
          <span className="stat-tile-label">{c.label}</span>
        </div>
      ))}

      <div className="card difficulty-best-tile">
        <span className="diff-best-title">Best WPM by difficulty</span>
        <div className="diff-best-row">
          <div className="diff-best-item">
            <span className="diff-best-value diff-easy">{stats.bestWpmEasy ?? '—'}</span>
            <span className="diff-best-label">Easy</span>
          </div>
          <div className="diff-best-item">
            <span className="diff-best-value diff-medium">{stats.bestWpmMedium ?? '—'}</span>
            <span className="diff-best-label">Medium</span>
          </div>
          <div className="diff-best-item">
            <span className="diff-best-value diff-hard">{stats.bestWpmHard ?? '—'}</span>
            <span className="diff-best-label">Hard</span>
          </div>
        </div>
      </div>
    </div>
  );
}
