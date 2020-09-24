export const setAccessTokenCookie = (accessToken) => {
  document.cookie = `accessToken=${accessToken}; path=/;`;
};

export const removeAccessTokenCookie = () => {
  document.cookie = `accessToken=; expires=Thu, 01 Jan 1999 00:00:10 GMT;`;
};

export const getAccessTokenFromCookie = () => {
  if (document.cookie) {
    const array = document.cookie.split('accessToken=');
    if (array.length >= 2) {
      const arraySub = array[1].split(';');
      return arraySub[0];
    }
  }
  return '';
};
