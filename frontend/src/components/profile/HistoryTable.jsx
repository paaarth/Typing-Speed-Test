import { formatLabel, formatDate } from '../../utils/format.js';

export default function HistoryTable({ results }) {
  if (results.length === 0) {
    return <p className="empty-text">No tests in this filter yet.</p>;
  }

  return (
    <div className="history-table-wrap">
      <table className="history-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Topic</th>
            <th>Difficulty</th>
            <th>WPM</th>
            <th>Accuracy</th>
            <th>Errors</th>
          </tr>
        </thead>
        <tbody>
          {results.map((r) => (
            <tr key={r.id}>
              <td>{formatDate(r.testDate)}</td>
              <td>{formatLabel(r.topic)}</td>
              <td>
                <span className={`diff-pill diff-pill-${r.difficulty.toLowerCase()}`}>
                  {formatLabel(r.difficulty)}
                </span>
              </td>
              <td className="mono-cell">{r.wpm}</td>
              <td className="mono-cell">{r.accuracy}%</td>
              <td className="mono-cell">{r.errors}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
