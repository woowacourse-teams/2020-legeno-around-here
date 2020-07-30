export const setAccessTokenCookie = (accessToken) => {
  document.cookie = `accessToken=${accessToken}`;
};

export const getAccessTokenFromCookie = () => {
  if (document.cookie) {
    const array = document.cookie.split("accessToken=");
    if (array.length >= 2) {
      const arraySub = array[1].split(";");
      return arraySub[0];
    }
  }
  return "";
};
