const STATS = [
  { key: 'wpm', label: 'WPM', accent: 'strawberry', format: (v) => v },
  { key: 'accuracy', label: 'Accuracy', accent: 'mint', format: (v) => `${v}%` },
  { key: 'timeTakenSeconds', label: 'Time', accent: 'blueberry', format: (v) => `${v}s` },
  { key: 'errors', label: 'Errors', accent: 'banana', format: (v) => v },
];

export default function LiveStats({ wpm, accuracy, timeTakenSeconds, errors }) {
  const values = { wpm, accuracy, timeTakenSeconds, errors };

  return (
    <div className="live-stats">
      {STATS.map((s) => (
        <div key={s.key} className={`stat-card accent-${s.accent}`}>
          <span className="stat-value">{s.format(values[s.key])}</span>
          <span className="stat-label">{s.label}</span>
        </div>
      ))}
    </div>
  );
}
