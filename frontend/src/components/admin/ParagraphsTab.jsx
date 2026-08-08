import { useEffect, useMemo, useState } from 'react';
import { Trash2, Pencil, Plus, X } from 'lucide-react';
import {
  fetchAdminParagraphs, createParagraph, updateParagraph, deleteParagraph,
  fetchAdminTopics, fetchWordLimits,
} from '../../api/adminApi.js';
import { formatLabel } from '../../utils/format.js';

const DIFFICULTIES = ['EASY', 'MEDIUM', 'HARD'];

function countWords(text) {
  const trimmed = text.trim();
  return trimmed ? trimmed.split(/\s+/).length : 0;
}

export default function ParagraphsTab() {
  const [paragraphs, setParagraphs] = useState([]);
  const [topics, setTopics] = useState([]);
  const [wordLimits, setWordLimits] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [topicFilter, setTopicFilter] = useState('ALL');

  const [formOpen, setFormOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [text, setText] = useState('');
  const [topicId, setTopicId] = useState('');
  const [difficulty, setDifficulty] = useState('EASY');
  const [saving, setSaving] = useState(false);

  // Each request is independent — a failure in one (e.g. paragraphs, if some
  // pre-date the topics table) no longer blocks the others from loading.
  const load = () => {
    setLoading(true);
    Promise.allSettled([fetchAdminParagraphs(), fetchAdminTopics(), fetchWordLimits()])
      .then(([paragraphsResult, topicsResult, limitsResult]) => {
        if (paragraphsResult.status === 'fulfilled') {
          setParagraphs(paragraphsResult.value);
          setError('');
        } else {
          setError(paragraphsResult.reason?.message || 'Could not load paragraphs.');
        }

        if (topicsResult.status === 'fulfilled') {
          setTopics(topicsResult.value);
          setTopicId((current) => current || (topicsResult.value.length > 0 ? String(topicsResult.value[0].id) : ''));
        }

        if (limitsResult.status === 'fulfilled') {
          setWordLimits(limitsResult.value);
        }
      })
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const resetForm = () => {
    setEditingId(null);
    setText('');
    setDifficulty('EASY');
    setTopicId(topics.length > 0 ? String(topics[0].id) : '');
    setFormOpen(false);
  };

  const startEdit = (p) => {
    if (!p.topicId) return; // orphaned row — can only be deleted, not edited
    setEditingId(p.id);
    setText(p.text);
    setTopicId(String(p.topicId));
    setDifficulty(p.difficulty);
    setFormOpen(true);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      const payload = { text, topicId: Number(topicId), difficulty };
      if (editingId) {
        await updateParagraph(editingId, payload);
      } else {
        await createParagraph(payload);
      }
      resetForm();
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this paragraph?')) return;
    setError('');
    try {
      await deleteParagraph(id);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const wordCount = countWords(text);
  const limit = wordLimits[difficulty];
  const withinLimit = !limit || (wordCount >= limit[0] && wordCount <= limit[1]);

  const visibleParagraphs = useMemo(() => {
    if (topicFilter === 'ALL') return paragraphs;
    return paragraphs.filter((p) => p.topicName === topicFilter);
  }, [paragraphs, topicFilter]);

  if (loading) return <p className="loading-text">Loading paragraphs…</p>;

  return (
    <div>
      {error && <p className="error-banner">{error}</p>}

      {topics.length > 0 && (
        <div className="admin-topic-filter">
          <button
            type="button"
            className={`keycap filter-btn ${topicFilter === 'ALL' ? 'keycap-blueberry' : 'keycap-ghost'}`}
            onClick={() => setTopicFilter('ALL')}
          >
            All topics
          </button>
          {topics.map((t) => (
            <button
              key={t.id}
              type="button"
              className={`keycap filter-btn ${topicFilter === t.name ? 'keycap-blueberry' : 'keycap-ghost'}`}
              onClick={() => setTopicFilter(t.name)}
            >
              {formatLabel(t.name)}
            </button>
          ))}
        </div>
      )}

      {!formOpen && (
        <button type="button" className="keycap keycap-strawberry admin-add-btn" onClick={() => setFormOpen(true)}>
          <Plus size={16} /> Add paragraph
        </button>
      )}

      {formOpen && (
        <form className="card admin-form" onSubmit={handleSubmit}>
          <div className="admin-form-row">
            <select
              className="admin-select"
              value={topicId}
              onChange={(e) => setTopicId(e.target.value)}
              disabled={topics.length === 0}
            >
              {topics.length === 0 && <option value="">Create a topic first</option>}
              {topics.map((t) => (
                <option key={t.id} value={t.id}>{t.name}</option>
              ))}
            </select>
            <select className="admin-select" value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
              {DIFFICULTIES.map((d) => <option key={d} value={d}>{d}</option>)}
            </select>
          </div>
          <textarea
            className="admin-textarea"
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Paragraph text…"
            rows={4}
            required
          />
          <p className={`word-count-hint ${withinLimit ? '' : 'word-count-hint-warn'}`}>
            {wordCount} words
            {limit && ` — ${difficulty.toLowerCase()} paragraphs should be ${limit[0]}-${limit[1]}`}
          </p>
          <div className="admin-form-actions">
            <button type="button" className="keycap keycap-ghost" onClick={resetForm}>
              <X size={16} /> Cancel
            </button>
            <button type="submit" className="keycap keycap-mint" disabled={saving || topics.length === 0}>
              {saving ? 'Saving…' : editingId ? 'Save changes' : 'Create paragraph'}
            </button>
          </div>
        </form>
      )}

      <div className="admin-list">
        {visibleParagraphs.length === 0 && <p className="empty-text">No paragraphs here yet.</p>}
        {visibleParagraphs.map((p) => (
          <div key={p.id} className="admin-row">
            <div className="admin-row-meta">
              <span className={`admin-row-topic ${!p.topicId ? 'admin-row-topic-orphan' : ''}`}>{p.topicName}</span>
              <span className={`diff-pill diff-pill-${p.difficulty.toLowerCase()}`}>{p.difficulty}</span>
              <span className="admin-row-words">{p.wordCount} words</span>
            </div>
            <p className="admin-row-text">{p.text}</p>
            <div className="admin-row-actions">
              {p.topicId && (
                <button className="keycap keycap-ghost admin-icon-btn" onClick={() => startEdit(p)} aria-label="Edit paragraph">
                  <Pencil size={15} />
                </button>
              )}
              <button className="keycap keycap-ghost admin-icon-btn" onClick={() => handleDelete(p.id)} aria-label="Delete paragraph">
                <Trash2 size={15} />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
