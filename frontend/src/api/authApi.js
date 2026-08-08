import client from './client.js';

export async function loginUser(username, password) {
  const { data } = await client.post('/auth/login', { username, password });
  return data;
}

export async function registerUser(username, password, email) {
  const { data } = await client.post('/auth/register', { username, password, email });
  return data;
}
