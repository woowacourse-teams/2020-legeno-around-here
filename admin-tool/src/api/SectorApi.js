import API, { makeTokenHeader, redirectInvalidToken } from './Api';
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
    .catch(() => {
      history.push('/');
    })
    .then(() => {
      setLoading(false);
    });
};

export const findSector = (cookies, removeCookie, closeModal, rowId, setLoading, setSectorDetails) => {
  setLoading(true);

  API.get(`/admin/sectors/${rowId}`, makeTokenHeader(cookies))
    .then((response) => {
      const data = response.data;
      setSectorDetails({
        id: data.id,
        name: data.name,
        description: data.description,
        createdAt: data.createdAt,
        creatorId: data.creator.id,
        creatorEmail: data.creator.email,
        creatorNickname: data.creator.nickname,
        lastModifiedAt: data.lastModifiedAt,
        lastModifierId: data.lastModifier.id,
        lastModifierEmail: data.lastModifier.email,
        lastModifierNickname: data.lastModifier.nickname,
        state: data.state,
        reason: data.reason,
      });
    })
    .catch(() => {
      closeModal();
    })
    .then(() => {
      setLoading(false);
    });
};

export const updateSectorState = (
  cookies,
  removeCookie,
  rowId,
  updateStateAndReason,
  defaultStateAndReason,
  setUpdateStateAndReason,
  rerenderStandard,
  setRerenderStandard,
) => {
  const data = {
    state: updateStateAndReason.state,
    reason: updateStateAndReason.reason,
  };

  API.put(`/admin/sectors/${rowId}/state`, data, makeTokenHeader(cookies))
    .then((response) => {
      if (response.status === 200) {
        console.log('200');
        setUpdateStateAndReason(defaultStateAndReason);
        setRerenderStandard(!rerenderStandard);
      }
    })
    .catch(() => {});
};
