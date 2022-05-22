import dayjs from 'dayjs/esm';
import { IInvestor } from 'app/entities/investor/investor.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ITransaction {
  id?: number;
  amount?: number;
  createdOn?: dayjs.Dayjs | null;
  type?: TransactionType | null;
  user?: IInvestor;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public amount?: number,
    public createdOn?: dayjs.Dayjs | null,
    public type?: TransactionType | null,
    public user?: IInvestor
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
