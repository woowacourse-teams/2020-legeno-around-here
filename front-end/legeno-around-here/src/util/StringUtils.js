const ellipsisText = '...';

export const ellipsis = (text, ellipsisSize) => {
  if (text.length <= ellipsisSize) {
    return text;
  }
  const shortenedText = text.substring(0, ellipsisSize);
  return shortenedText.concat(ellipsisText);
};
