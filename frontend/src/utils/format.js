export function formatLabel(value) {
  if (!value) return '';
  return value.charAt(0) + value.slice(1).toLowerCase();
}

export function formatDate(isoString) {
  const date = new Date(isoString);
  const day = date.toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
  const time = date.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit' });
  return `${day} · ${time}`;
}
