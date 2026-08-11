import { getTopicIcon } from '../../utils/topicIcons.js';
import { formatLabel } from '../../utils/format.js';

const ACCENTS = ['strawberry', 'mint', 'blueberry', 'lavender', 'banana'];
const DIFFICULTIES = ['EASY', 'MEDIUM', 'HARD'];

export default function TopicDifficultySelector({ topics, topic, difficulty, onTopicChange, onDifficultyChange }) {
  return (
    <div className="selector-block">
      <p className="selector-label">Pick a topic</p>
      <div className="topic-grid">
        {topics.map((t, index) => {
          const Icon = getTopicIcon(t.icon);
          const accent = ACCENTS[index % ACCENTS.length];
          const isActive = t.name === topic;
          return (
            <button
              key={t.id}
              type="button"
              className={`topic-card accent-${accent} ${isActive ? 'is-active' : ''}`}
              onClick={() => onTopicChange(t.name)}
              aria-pressed={isActive}
            >
              <Icon size={22} strokeWidth={2.2} />
              <span>{formatLabel(t.name)}</span>
            </button>
          );
        })}
      </div>

      <p className="selector-label">Choose a difficulty</p>
      <div className="difficulty-row">
        {DIFFICULTIES.map((d) => (
          <button
            key={d}
            type="button"
            className={`keycap difficulty-btn ${d === difficulty ? 'keycap-mint' : 'keycap-ghost'}`}
            onClick={() => onDifficultyChange(d)}
            aria-pressed={d === difficulty}
          >
            {formatLabel(d)}
          </button>
        ))}
      </div>
    </div>
  );
}
