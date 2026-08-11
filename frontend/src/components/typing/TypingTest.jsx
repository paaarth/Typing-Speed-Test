import { useEffect, useRef, useState, useCallback } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { RefreshCw, ArrowLeft } from 'lucide-react';
import { fetchRandomParagraph } from '../../api/paragraphApi.js';
import { submitResult } from '../../api/resultApi.js';
import { useAuth } from '../../context/AuthContext.jsx';
import { useTypingEngine } from '../../hooks/useTypingEngine.js';
import ParagraphDisplay from './ParagraphDisplay.jsx';
import LiveStats from './LiveStats.jsx';
import ResultSummary from './ResultSummary.jsx';
import { formatLabel } from '../../utils/format.js';
import './TypingTest.css';

export default function TypingTest() {
  const location = useLocation();
  const { isAuthenticated } = useAuth();

  const [topic] = useState(location.state?.topic || 'TECHNOLOGY');
  const [difficulty] = useState(location.state?.difficulty || 'EASY');
  const [paragraph, setParagraph] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isFocused, setIsFocused] = useState(false);
  const [saveState, setSaveState] = useState('idle'); // idle | saving | saved | error

  const inputRef = useRef(null);
  const paragraphText = paragraph?.text || '';
  const engine = useTypingEngine(paragraphText);

  const loadParagraph = useCallback((t, d) => {
    setLoading(true);
    setError('');
    setSaveState('idle');
    fetchRandomParagraph(t, d)
      .then(setParagraph)
      .catch(() => setError('Could not load a paragraph. Is the backend running on port 8080?'))
      .finally(() => setLoading(false));
  }, []);

  // Load the first paragraph once, on mount.
  useEffect(() => {
    loadParagraph(topic, difficulty);
  }, [loadParagraph, topic, difficulty]);

  // Persist the result the moment a test completes (signed-in users only).
  useEffect(() => {
    if (!engine.isFinished || !paragraph || !isAuthenticated) return;

    setSaveState('saving');
    submitResult({
      paragraphId: paragraph.id,
      wpm: engine.wpm,
      accuracy: engine.accuracy,
      errors: engine.errors,
      timeTakenSeconds: engine.timeTakenSeconds,
    })
      .then(() => setSaveState('saved'))
      .catch(() => setSaveState('error'));
  }, [engine.isFinished]);

  const handleNewParagraph = () => loadParagraph(topic, difficulty);
  const handleTryAgain = () => engine.reset();
  const focusInput = () => inputRef.current?.focus();

  return (
    <div className="container">
      <div className="test-header">
        <Link to="/" className="back-link">
          <ArrowLeft size={16} /> Change topic
        </Link>
        {paragraph && (
          <span className="test-badge">
            {formatLabel(topic)} · {formatLabel(difficulty)}
          </span>
        )}
      </div>

      <LiveStats
        wpm={engine.wpm}
        accuracy={engine.accuracy}
        timeTakenSeconds={engine.timeTakenSeconds}
        errors={engine.errors}
      />

      {loading && <p className="loading-text">Loading paragraph…</p>}
      {error && <p className="error-banner">{error}</p>}

      {!loading && !error && paragraph && !engine.isFinished && (
        <div className={`paragraph-stage ${isFocused ? 'is-focused' : ''}`} onClick={focusInput}>
          <ParagraphDisplay text={paragraphText} typedText={engine.typedText} />
          {!isFocused && <p className="stage-hint">Click here and start typing</p>}
          <input
            ref={inputRef}
            className="hidden-input"
            value={engine.typedText}
            onChange={(e) => engine.handleChange(e.target.value)}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
            autoComplete="off"
            autoCapitalize="off"
            autoCorrect="off"
            spellCheck="false"
            aria-label="Type the paragraph shown above"
          />
        </div>
      )}

      {engine.isFinished && (
        <ResultSummary
          result={{
            wpm: engine.wpm,
            accuracy: engine.accuracy,
            timeTakenSeconds: engine.timeTakenSeconds,
            errors: engine.errors,
          }}
          isAuthenticated={isAuthenticated}
          saveState={saveState}
          onTryAgain={handleTryAgain}
          onNewParagraph={handleNewParagraph}
        />
      )}

      {!engine.isFinished && paragraph && (
        <button className="keycap keycap-ghost refresh-btn" onClick={handleNewParagraph}>
          <RefreshCw size={15} /> New paragraph
        </button>
      )}
    </div>
  );
}
