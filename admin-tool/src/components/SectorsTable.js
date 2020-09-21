import React, { useEffect, useRef, useState } from 'react';
import { useCookies } from 'react-cookie';
import { findAllSectors } from '../api/SectorApi';
import { withRouter } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { produce } from 'immer';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Button from '@material-ui/core/Button';
import SectorModal from './SectorModal';
import * as qs from 'qs';

const columns = [
  {
    id: 'id',
    label: 'ID',
    align: 'center',
    minWidth: 50,
    sortedBy: 'id',
  },
  {
    id: 'name',
    label: '부문명',
    minWidth: 150,
    sortedBy: 'name',
  },
  {
    id: 'description',
    label: '설명',
    minWidth: 300,
    sortedBy: 'description',
  },
  {
    id: 'creator',
    label: '창시자',
    align: 'center',
    minWidth: 100,
    sortedBy: 'creator',
  },
  {
    id: 'createdAt',
    label: '창시 일자',
    align: 'center',
    minWidth: 100,
    sortedBy: 'createdAt',
  },
  {
    id: 'state',
    label: '상태',
    align: 'center',
    minWidth: 50,
    sortedBy: 'state',
  },
  {
    id: 'lastModifier',
    label: '상태 수정자',
    align: 'center',
    minWidth: 100,
    sortedBy: 'creator',
  },
];

export const createRows = (contents) => {
  const createRow = (content) => {
    return {
      id: content.id,
      name: content.name,
      description: content.description,
      creator: content.creator.nickname,
      createdAt: content.createdAt.substr(0, 10),
      lastModifier: content.lastModifier.nickname,
      state: content.state,
    };
  };

  const rows = new Array(contents.length);

  let index = 0;

  contents.forEach((content) => {
    rows[index] = createRow(content);
    index++;
  });

  return rows;
};

const useStyles = makeStyles(() => ({
  root: {
    width: '100%',
  },
  container: {
    maxHeight: '100%',
  },
  formControl: {
    minWidth: 120,
  },
}));

const SectorsTable = ({ location, history }) => {
  const classes = useStyles();

  const [cookies, removeCookie] = useCookies(['accessToken']);
  const [loading, setLoading] = useState(false);
  const [rows, setRows] = useState(null);

  const getInitialPageProperty = () => {
    const query = qs.parse(location.search, {
      ignoreQueryPrefix: true,
    });

    return {
      page: query.page ? parseInt(query.page) - 1 : 0,
      totalPages: 0,
      size: query.size ? parseInt(query.size) : 10,
      sortedBy: query.sortedBy ? query.sortedBy : 'id',
      direction: query.direction ? query.direction : 'desc',
      totalElements: 0,
      updateHistory: false,
    };
  };

  const [pageProperty, setPageProperty] = useState(getInitialPageProperty());
  const [open, setOpen] = React.useState(false);
  const [rowId, setRowId] = React.useState(null);

  useEffect(() => {
    if (open) {
      return;
    }
    const fetchData = async () =>
      await findAllSectors(
        history,
        cookies,
        removeCookie,
        setLoading,
        setRows,
        getInitialPageProperty(),
        setPageProperty,
      );
    fetchData();
    // eslint-disable-next-line
  }, [cookies, history, location, open]);

  const mounted = useRef(false);

  useEffect(() => {
    if (!mounted || !pageProperty.updateHistory) {
      mounted.current = true;
      return;
    }
    const parameter = qs.stringify({
      page: pageProperty.page + 1,
      size: pageProperty.size,
      sortedBy: pageProperty.sortedBy,
      direction: pageProperty.direction,
    });
    const url = location.pathname + '?' + parameter;
    mounted.current = false;

    setPageProperty(
      produce(pageProperty, (draft) => {
        draft['updateHistory'] = false;
      }),
    );

    history.push(url);
    // eslint-disable-next-line
  }, [pageProperty.updateHistory]);

  const onChangeOfSize = (event) => {
    event.preventDefault();
    setPageProperty(
      produce(pageProperty, (draft) => {
        draft['size'] = parseInt(event.currentTarget.value);
        draft['page'] = 0;
        draft['updateHistory'] = true;
      }),
    );
  };

  const onClickOfTableHeader = (event) => {
    event.preventDefault();
    const { sortedBy } = event.currentTarget.dataset;

    const getDirection = () => {
      if (pageProperty.direction === 'desc') {
        return 'asc';
      }
      return 'desc';
    };

    if (sortedBy === pageProperty.sortedBy) {
      const direction = getDirection();
      setPageProperty(
        produce(pageProperty, (draft) => {
          draft['direction'] = direction;
          draft['updateHistory'] = true;
        }),
      );
      return;
    }

    setPageProperty(
      produce(pageProperty, (draft) => {
        draft['sortedBy'] = sortedBy;
        draft['direction'] = 'desc';
        draft['updateHistory'] = true;
      }),
    );
  };

  const onClickOfPage = (event) => {
    event.preventDefault();
    const { value } = event.currentTarget;
    let result = pageProperty.page + value * 1;

    if (result >= pageProperty.totalPages) {
      result = pageProperty.totalPages - 1;
    }

    if (result < 0) {
      result = 0;
    }

    setPageProperty(
      produce(pageProperty, (draft) => {
        draft['page'] = result;
        draft['updateHistory'] = true;
      }),
    );
  };

  if (loading) {
    return <>로딩중</>;
  }

  if (!rows) {
    return null;
  }

  const openModal = (event) => {
    event.preventDefault();
    const { rowId } = event.currentTarget.dataset;
    setRowId(rowId);
    setOpen(true);
  };

  const closeModal = (event) => {
    event.preventDefault();
    setOpen(false);
    setRowId(null);
  };

  return (
    <>
      <Paper className={classes.root}>
        <TableContainer className={classes.container}>
          <Table stickyHeader aria-label='sticky table'>
            <caption>
              <Button variant='outlined' size='small' value='-10' onClick={onClickOfPage}>
                ᐊᐊ
              </Button>
              &nbsp;
              <Button variant='outlined' size='small' value='-1' onClick={onClickOfPage}>
                ᐊ
              </Button>
              &ensp;{pageProperty.page + 1} / {pageProperty.totalPages}&ensp;
              <Button variant='outlined' size='small' value='+1' onClick={onClickOfPage}>
                ᐅ
              </Button>
              &nbsp;
              <Button variant='outlined' size='small' value='+10' onClick={onClickOfPage}>
                ᐅᐅ
              </Button>
              &emsp;총 {pageProperty.totalElements}건&emsp; 조회 개수 : &nbsp;
              <FormControl className={classes.formControl}>
                <Select native value={pageProperty.size} onChange={onChangeOfSize}>
                  <option aria-label='None' value='' />
                  <option value={10}>10</option>
                  <option value={25}>25</option>
                  <option value={50}>50</option>
                  <option value={100}>100</option>
                </Select>
              </FormControl>
            </caption>
            <TableHead>
              <TableRow>
                {columns.map((column) => (
                  <TableCell
                    key={column.id}
                    align={column.align}
                    style={{ minWidth: column.minWidth }}
                    data-sorted-by={column.sortedBy}
                    onClick={onClickOfTableHeader}
                  >
                    {column.label}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => {
                return (
                  <TableRow hover role='checkbox' tabIndex={-1} key={row.id} data-row-id={row.id} onClick={openModal}>
                    {columns.map((column) => {
                      const value = row[column.id];
                      return (
                        <TableCell key={column.id} align={column.align}>
                          {column.format && typeof value === 'number' ? column.format(value) : value}
                        </TableCell>
                      );
                    })}
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
        <SectorModal open={open} closeModal={closeModal} rowId={rowId} />
      </Paper>
    </>
  );
};

export default withRouter(SectorsTable);
