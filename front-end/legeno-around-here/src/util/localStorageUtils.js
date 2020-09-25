const allId = '';

const mainAreaId = 'mainAreaId';
const mainAreaName = 'mainAreaName';
const allAreaName = '대한민국';

const mainSectorId = 'mainSectorId';
const mainSectorName = 'mainSectorName';
const allSectorName = '전체 부문';

const mainCriteria = 'mainCriteria';
const allCriteria = 'total';

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

export const getMainSectorId = () => {
  localStorage.getItem(mainSectorId) || localStorage.setItem(mainSectorId, allId);
  return localStorage.getItem(mainSectorId);
};

export const getMainSectorName = () => {
  localStorage.getItem(mainSectorName) || localStorage.setItem(mainSectorName, allSectorName);
  return localStorage.getItem(mainSectorName);
};

export const setMainSectorId = (sectorId) => {
  localStorage.setItem(mainSectorId, sectorId);
};

export const setMainSectorName = (sectorName) => {
  localStorage.setItem(mainSectorName, sectorName);
};

export const getMainCriteria = () => {
  localStorage.getItem(mainCriteria) || localStorage.setItem(mainCriteria, allCriteria);
  return localStorage.getItem(mainCriteria);
};

export const setMainCriteria = (criteria) => {
  localStorage.setItem(mainCriteria, criteria);
};
