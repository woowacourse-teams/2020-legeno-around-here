export const getMainAreaName = () => {
  localStorage.getItem('mainAreaName') || localStorage.setItem('mainAreaName', '서울특별시');
  return localStorage.getItem('mainAreaName');
};

export const getMainAreaId = () => {
  localStorage.getItem('mainAreaId') || localStorage.setItem('mainAreaId', '1');
  return localStorage.getItem('mainAreaId');
};
