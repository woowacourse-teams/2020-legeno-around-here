import API, { makeHeaderConfig, redirectInvalidToken } from './Api';

export const findAllSectors = (history, cookies, removeCookie, setData, setLoading) => {
  setLoading(true);
  const config = makeHeaderConfig(cookies);

  API.get('/admin/sectors', config)
    .then((response) => {
      console.log('success');
      console.log(response.data);
      setData(response.data);
    })
    .catch((error) => {
      let status = error.response.status;
      redirectInvalidToken(history, status, removeCookie);
      console.log('fail');
      return null;
    })
    .then(() => {
      setLoading(false);
    });
};
