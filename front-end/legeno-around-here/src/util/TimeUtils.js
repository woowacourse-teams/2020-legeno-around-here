export const convertDateFormat = (UTCDate) => {
  const dateFormat = UTCDate.split('T');
  const yyyymmdd = dateFormat[0];
  const hh = dateFormat[1].split(':')[0];
  const mm = dateFormat[1].split(':')[1];
  return yyyymmdd + ' ' + hh + '시' + mm + '분';
};
