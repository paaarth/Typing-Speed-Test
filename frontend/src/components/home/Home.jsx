import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { fetchTopics } from '../../api/paragraphApi.js';
import TopicDifficultySelector from './TopicDifficultySelector.jsx';
import './Home.css';

const DEMO_TEXT = 'the quick fox jumps over lazy dogs';

function HeroTyper() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    const delay = count < DEMO_TEXT.length ? 85 : 1400;
    const next = count < DEMO_TEXT.length ? count + 1 : 0;
    const timer = setTimeout(() => setCount(next), delay);
    return () => clearTimeout(timer);
  }, [count]);

  return (
    <div className="hero-typer" aria-hidden="true">
      {DEMO_TEXT.split('').map((ch, i) => (
        <span
          key={i}
          className={i < count ? 'ht-done' : i === count ? 'ht-current' : 'ht-pending'}
        >
          {ch}
        </span>
      ))}
    </div>
  );
}

export default function Home() {
  const navigate = useNavigate();
  const [topics, setTopics] = useState([]);
  const [topic, setTopic] = useState('TECHNOLOGY');
  const [difficulty, setDifficulty] = useState('EASY');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTopics()
      .then((data) => {
        setTopics(data);
        if (data.length > 0) setTopic(data[0].name);
      })
      .catch(() => setError('Could not reach the server. Is the backend running on port 8080?'))
      .finally(() => setLoading(false));
  }, []);

  const handleStart = () => {
    navigate('/test', { state: { topic, difficulty } });
  };

  return (
    <div className="container">
      <section className="hero">
        <p className="hero-eyebrow">Free typing practice</p>
        <h1 className="hero-title">Type something you actually want to read.</h1>
        <p className="hero-subtitle">
          Pick a topic, choose a difficulty, and watch your speed and accuracy update as you go.
        </p>
        <HeroTyper />
      </section>

      <section className="card selector-card">
        {loading && <p className="loading-text">Loading topics…</p>}
        {error && <p className="error-banner">{error}</p>}

        {!loading && !error && (
          <>
            <TopicDifficultySelector
              topics={topics}
              topic={topic}
              difficulty={difficulty}
              onTopicChange={setTopic}
              onDifficultyChange={setDifficulty}
            />
            <button className="keycap keycap-strawberry keycap-spacebar start-btn" onClick={handleStart}>
              Start typing test <ArrowRight size={18} strokeWidth={2.5} />
            </button>
          </>
        )}
      </section>
    </div>
  );
}
