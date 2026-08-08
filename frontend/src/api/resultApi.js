import client from './client.js';

export async function submitResult(payload) {
  const { data } = await client.post('/results', payload);
  return data;
}

export async function fetchHistory() {
  const { data } = await client.get('/results/history');
  return data;
}

export async function fetchStats() {
  const { data } = await client.get('/results/stats');
  return data;
}
