import dayjs from 'dayjs/esm';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IInvestor {
  id?: number;
  name?: string;
  description?: string | null;
  email?: string;
  gender?: Gender;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  city?: string;
  country?: string;
  createdOn?: dayjs.Dayjs | null;
}

export class Investor implements IInvestor {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public email?: string,
    public gender?: Gender,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string | null,
    public city?: string,
    public country?: string,
    public createdOn?: dayjs.Dayjs | null
  ) {}
}

export function getInvestorIdentifier(investor: IInvestor): number | undefined {
  return investor.id;
}
