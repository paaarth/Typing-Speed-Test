import { useState, useCallback, useEffect } from 'react';

export function useTypingEngine(paragraphText) {
  const [typedText, setTypedText] = useState('');
  const [startTime, setStartTime] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [elapsed, setElapsed] = useState(0);

  const reset = useCallback(() => {
    setTypedText('');
    setStartTime(null);
    setEndTime(null);
    setElapsed(0);
  }, []);

  // whenever a new paragraph is loaded, it start clean.
  useEffect(() => {
    reset();
  }, [paragraphText, reset]);

  // live updating timer while a test is in progress.
  useEffect(() => {
    if (!startTime || endTime) return undefined;
    const interval = setInterval(() => setElapsed((Date.now() - startTime) / 1000), 200);
    return () => clearInterval(interval);
  }, [startTime, endTime]);

  const isFinished = Boolean(endTime);

  const handleChange = useCallback(
    (value) => {
      if (isFinished || value.length > paragraphText.length) return;
      if (!startTime && value.length > 0) setStartTime(Date.now());
      setTypedText(value);
      if (value.length === paragraphText.length) setEndTime(Date.now());
    },
    [isFinished, paragraphText, startTime]
  );

  let errors = 0;
  for (let i = 0; i < typedText.length; i++) {
    if (typedText[i] !== paragraphText[i]) errors++;
  }

  const seconds = endTime && startTime ? (endTime - startTime) / 1000 : elapsed;
  const minutes = Math.max(seconds, 0.001) / 60;
  const correctChars = typedText.length - errors;
  const wpm = typedText.length > 0 ? Math.max(0, Math.round(correctChars / 5 / minutes)) : 0;
  const accuracy = typedText.length > 0 ? Math.round((correctChars / typedText.length) * 100) : 100;

  return {
    typedText,
    handleChange,
    reset,
    isFinished,
    errors,
    wpm,
    accuracy,
    timeTakenSeconds: Math.round(seconds),
  };
}
