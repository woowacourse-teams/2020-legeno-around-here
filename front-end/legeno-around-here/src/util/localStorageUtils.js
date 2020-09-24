const allId = '';

const mainAreaId = 'mainAreaId';
const mainAreaName = 'mainAreaName';
const allAreaName = '대한민국';

export const getMainAreaId = () => {
  localStorage.getItem(mainAreaId) || localStorage.setItem(mainAreaId, allId);
  return localStorage.getItem(mainAreaId);
};

export const getMainAreaName = () => {
  localStorage.getItem(mainAreaName) || localStorage.setItem(mainAreaName, allAreaName);
  return localStorage.getItem(mainAreaName);
};

export const setMainAreaId = (areaId) => {
  localStorage.setItem(mainAreaId, areaId);
};

export const setMainAreaName = (areaName) => {
  localStorage.setItem(mainAreaName, areaName);
};

export const setMainAllArea = () => {
  localStorage.setItem(mainAreaId, allId);
  localStorage.setItem(mainAreaName, allAreaName);
};
