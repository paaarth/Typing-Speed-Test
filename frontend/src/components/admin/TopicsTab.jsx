import { useEffect, useState } from 'react';
import { Trash2, Pencil, Plus, X } from 'lucide-react';
import {
  fetchAdminTopics, createTopic, updateTopic, deleteTopic, fetchValidIcons,
} from '../../api/adminApi.js';
import { getTopicIcon } from '../../utils/topicIcons.js';

export default function TopicsTab() {
  const [topics, setTopics] = useState([]);
  const [icons, setIcons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [formOpen, setFormOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [name, setName] = useState('');
  const [icon, setIcon] = useState('');
  const [saving, setSaving] = useState(false);

  const load = () => {
    setLoading(true);
    Promise.all([fetchAdminTopics(), fetchValidIcons()])
      .then(([t, i]) => {
        setTopics(t);
        setIcons(i);
        setIcon((current) => current || (i.length > 0 ? i[0] : ''));
      })
      .catch(() => setError('Could not load topics.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const resetForm = () => {
    setEditingId(null);
    setName('');
    setIcon(icons.length > 0 ? icons[0] : '');
    setFormOpen(false);
  };

  const startEdit = (t) => {
    setEditingId(t.id);
    setName(t.name);
    setIcon(t.icon);
    setFormOpen(true);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      const payload = { name, icon };
      if (editingId) {
        await updateTopic(editingId, payload);
      } else {
        await createTopic(payload);
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
    if (!window.confirm('Delete this topic? This only works if no paragraphs use it.')) return;
    setError('');
    try {
      await deleteTopic(id);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <p className="loading-text">Loading topics…</p>;

  return (
    <div>
      {error && <p className="error-banner">{error}</p>}

      {!formOpen && (
        <button type="button" className="keycap keycap-strawberry admin-add-btn" onClick={() => setFormOpen(true)}>
          <Plus size={16} /> Add topic
        </button>
      )}

      {formOpen && (
        <form className="card admin-form" onSubmit={handleSubmit}>
          <input
            className="admin-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Topic name, e.g. TRAVEL"
            required
          />
          <div className="icon-picker">
            {icons.map((key) => {
              const Icon = getTopicIcon(key);
              return (
                <button
                  type="button"
                  key={key}
                  className={`icon-choice ${icon === key ? 'is-selected' : ''}`}
                  onClick={() => setIcon(key)}
                  aria-label={key}
                  aria-pressed={icon === key}
                >
                  <Icon size={18} />
                </button>
              );
            })}
          </div>
          <div className="admin-form-actions">
            <button type="button" className="keycap keycap-ghost" onClick={resetForm}>
              <X size={16} /> Cancel
            </button>
            <button type="submit" className="keycap keycap-mint" disabled={saving}>
              {saving ? 'Saving…' : editingId ? 'Save changes' : 'Create topic'}
            </button>
          </div>
        </form>
      )}

      <div className="admin-list">
        {topics.length === 0 && <p className="empty-text">No topics yet.</p>}
        {topics.map((t) => {
          const Icon = getTopicIcon(t.icon);
          return (
            <div key={t.id} className="admin-row admin-row-compact">
              <span className="admin-topic-icon"><Icon size={18} /></span>
              <span className="admin-row-topic">{t.name}</span>
              <div className="admin-row-actions">
                <button className="keycap keycap-ghost admin-icon-btn" onClick={() => startEdit(t)} aria-label="Edit topic">
                  <Pencil size={15} />
                </button>
                <button className="keycap keycap-ghost admin-icon-btn" onClick={() => handleDelete(t.id)} aria-label="Delete topic">
                  <Trash2 size={15} />
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
