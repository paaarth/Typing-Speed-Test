import { useState } from 'react';
import ParagraphsTab from './ParagraphsTab.jsx';
import TopicsTab from './TopicsTab.jsx';
import './AdminPanel.css';

export default function AdminPanel() {
  const [tab, setTab] = useState('paragraphs');

  return (
    <div className="container">
      <h1 className="page-title">Admin panel</h1>
      <p className="page-subtitle">Manage paragraphs and topics without touching the codebase.</p>

      <div className="admin-tabs">
        <button
          type="button"
          className={`keycap ${tab === 'paragraphs' ? 'keycap-blueberry' : 'keycap-ghost'}`}
          onClick={() => setTab('paragraphs')}
        >
          Paragraphs
        </button>
        <button
          type="button"
          className={`keycap ${tab === 'topics' ? 'keycap-blueberry' : 'keycap-ghost'}`}
          onClick={() => setTab('topics')}
        >
          Topics
        </button>
      </div>

      {tab === 'paragraphs' ? <ParagraphsTab /> : <TopicsTab />}
    </div>
  );
}
