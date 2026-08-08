export default function ParagraphDisplay({ text, typedText }) {
  return (
    <div className="paragraph-text">
      {text.split('').map((char, idx) => {
        let className = 'char-pending';
        if (idx < typedText.length) {
          className = typedText[idx] === char ? 'char-correct' : 'char-incorrect';
        } else if (idx === typedText.length) {
          className = 'char-current';
        }
        return (
          <span key={idx} className={className}>
            {char}
          </span>
        );
      })}
    </div>
  );
}
