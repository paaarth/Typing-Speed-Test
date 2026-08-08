import { Link } from 'react-router-dom';
import { RotateCcw, Shuffle } from 'lucide-react';

const RESULT_STATS = [
  { key: 'wpm', label: 'WPM', accent: 'strawberry', format: (v) => v },
  { key: 'accuracy', label: 'Accuracy', accent: 'mint', format: (v) => `${v}%` },
  { key: 'timeTakenSeconds', label: 'Time', accent: 'blueberry', format: (v) => `${v}s` },
  { key: 'errors', label: 'Errors', accent: 'banana', format: (v) => v },
];

export default function ResultSummary({ result, isAuthenticated, saveState, onTryAgain, onNewParagraph }) {
  return (
    <div className="card result-card">
      <h2 className="result-title">Nice work!</h2>

      <div className="result-stats">
        {RESULT_STATS.map((s) => (
          <div key={s.key} className="result-stat">
            <span className={`result-value result-value-${s.accent}`}>{s.format(result[s.key])}</span>
            <span className="result-label">{s.label}</span>
          </div>
        ))}
      </div>

      {isAuthenticated ? (
        <p className={`save-status ${saveState === 'error' ? 'save-status-error' : ''}`}>
          {saveState === 'saving' && 'Saving your result…'}
          {saveState === 'saved' && 'Saved to your profile ✓'}
          {saveState === 'error' && 'Could not save this result — is the backend running?'}
        </p>
      ) : (
        <p className="save-status">
          <Link to="/register">Sign up</Link> to save your results and track your progress.
        </p>
      )}

      <div className="result-actions">
        <button className="keycap keycap-ghost" onClick={onTryAgain}>
          <RotateCcw size={16} /> Try again
        </button>
      </div>
    </div>
  );
}
