import client from './client.js';

export async function fetchTopics() {
  const { data } = await client.get('/paragraphs/topics');
  return data;
}

export async function fetchRandomParagraph(topic, difficulty) {
  const { data } = await client.get('/paragraphs/random', {
    params: { topic, difficulty },
  });
  return data;
}
