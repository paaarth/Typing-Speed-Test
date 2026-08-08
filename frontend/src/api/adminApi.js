import client from './client.js';

// Paragraphs
export async function fetchAdminParagraphs() {
  const { data } = await client.get('/admin/paragraphs');
  return data;
}
export async function createParagraph(payload) {
  const { data } = await client.post('/admin/paragraphs', payload);
  return data;
}
export async function updateParagraph(id, payload) {
  const { data } = await client.put(`/admin/paragraphs/${id}`, payload);
  return data;
}
export async function deleteParagraph(id) {
  await client.delete(`/admin/paragraphs/${id}`);
}

// Topics
export async function fetchAdminTopics() {
  const { data } = await client.get('/admin/topics');
  return data;
}
export async function fetchWordLimits() {
  const { data } = await client.get('/admin/paragraphs/word-limits');
  return data;
}
export async function fetchValidIcons() {
  const { data } = await client.get('/admin/topics/icons');
  return data.icons;
}
export async function createTopic(payload) {
  const { data } = await client.post('/admin/topics', payload);
  return data;
}
export async function updateTopic(id, payload) {
  const { data } = await client.put(`/admin/topics/${id}`, payload);
  return data;
}
export async function deleteTopic(id) {
  await client.delete(`/admin/topics/${id}`);
}
