import API, { redirectInvalidToken } from './Api';
import { createRows } from '../components/SectorsTable';
import produce from 'immer';

export const findAllSectors = (history, cookies, removeCookie, setLoading, setRows, pageProperty, setPageProperty) => {
  setLoading(true);

  const config = {
    params: {
      page: pageProperty.page,
      size: pageProperty.size,
      sortedBy: pageProperty.sortedBy,
      direction: pageProperty.direction,
    },
    headers: {
      'X-Auth-Token': cookies['accessToken'],
    },
  };

  API.get('/admin/sectors', config)
    .then((response) => {
      const data = response.data;
      const contents = data.content;
      setRows(createRows(contents));

      setPageProperty(
        produce(pageProperty, (draft) => {
          draft['totalPages'] = data.totalPages;
          draft['totalElements'] = data.totalElements;
        }),
      );
    })
    .catch((error) => {
      let status = error.response.status;
      redirectInvalidToken(history, status, removeCookie);
    })
    .then(() => {
      setLoading(false);
    });
};
