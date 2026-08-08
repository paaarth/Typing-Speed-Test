import { useEffect, useState } from 'react';
import { Trash2, Pencil, Plus, X } from 'lucide-react';
import {
  fetchAdminParagraphs, createParagraph, updateParagraph, deleteParagraph, fetchAdminTopics,
} from '../../api/adminApi.js';

const DIFFICULTIES = ['EASY', 'MEDIUM', 'HARD'];

export default function ParagraphsTab() {
  const [paragraphs, setParagraphs] = useState([]);
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [formOpen, setFormOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [text, setText] = useState('');
  const [topicId, setTopicId] = useState('');
  const [difficulty, setDifficulty] = useState('EASY');
  const [saving, setSaving] = useState(false);

  const load = () => {
    setLoading(true);
    Promise.all([fetchAdminParagraphs(), fetchAdminTopics()])
      .then(([p, t]) => {
        setParagraphs(p);
        setTopics(t);
        setTopicId((current) => current || (t.length > 0 ? String(t[0].id) : ''));
      })
      .catch(() => setError('Could not load paragraphs.'))
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

  if (loading) return <p className="loading-text">Loading paragraphs…</p>;

  return (
    <div>
      {error && <p className="error-banner">{error}</p>}

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
            placeholder="Paragraph text (20-2000 characters)…"
            rows={4}
            required
          />
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
        {paragraphs.length === 0 && <p className="empty-text">No paragraphs yet.</p>}
        {paragraphs.map((p) => (
          <div key={p.id} className="admin-row">
            <div className="admin-row-meta">
              <span className="admin-row-topic">{p.topicName}</span>
              <span className={`diff-pill diff-pill-${p.difficulty.toLowerCase()}`}>{p.difficulty}</span>
              <span className="admin-row-words">{p.wordCount} words</span>
            </div>
            <p className="admin-row-text">{p.text}</p>
            <div className="admin-row-actions">
              <button className="keycap keycap-ghost admin-icon-btn" onClick={() => startEdit(p)} aria-label="Edit paragraph">
                <Pencil size={15} />
              </button>
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
