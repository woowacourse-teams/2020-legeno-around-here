import React from 'react';
import Link from '@material-ui/core/Link';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Title from './Title';

// Generate Order Data
const createData = (id, date, name, shipTo, paymentMethod, amount) => {
  return { id, date, name, shipTo, paymentMethod, amount };
}

const rows = [
  createData(0, '17 Sep, 2022', 'Elvis Presley', 'Tupelo, MS', 'VISA ⠀•••• 3719', 312.44),
  createData(1, '17 Sep, 2022', 'Paul McCartney', 'London, UK', 'VISA ⠀•••• 2574', 866.99),
  createData(2, '17 Sep, 2022', 'Tom Scholz', 'Boston, MA', 'MC ⠀•••• 1253', 100.81),
  createData(3, '17 Sep, 2022', 'Michael Jackson', 'Gary, IN', 'AMEX ⠀•••• 2000', 654.39),
  createData(4, '17 Sep, 2022', 'Bruce Springsteen', 'Long Branch, NJ', 'VISA ⠀•••• 5919', 212.79),
];

const preventDefault = (event) => {
  event.preventDefault();
}

const useStyles = makeStyles((theme) => ({
  seeMore: {
    marginTop: theme.spacing(3),
  },
}));

const Payments = () => {
  const classes = useStyles();
  return (
    <React.Fragment>
      <Title>우리동네캡짱 사용자 결제 내역</Title>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Date</TableCell>
            <TableCell>Name</TableCell>
            <TableCell>Ship To</TableCell>
            <TableCell>Payment Method</TableCell>
            <TableCell align="right">Sale Amount</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.id}>
              <TableCell>{row.date}</TableCell>
              <TableCell>{row.name}</TableCell>
              <TableCell>{row.shipTo}</TableCell>
              <TableCell>{row.paymentMethod}</TableCell>
              <TableCell align="right">{row.amount}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className={classes.seeMore}>
        <Link color="primary" href="#" onClick={preventDefault}>
          사용자 결제내역 더보기
        </Link>
      </div>
    </React.Fragment>
  );
}

export default Payments;
