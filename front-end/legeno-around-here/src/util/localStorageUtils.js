export const getMainAreaName = () => {
  localStorage.getItem('mainAreaName') || localStorage.setItem('mainAreaName', '전체');
  return localStorage.getItem('mainAreaName');
};

export const getMainAreaId = () => {
  localStorage.getItem('mainAreaId') || localStorage.setItem('mainAreaId', '');
  return localStorage.getItem('mainAreaId');
};
