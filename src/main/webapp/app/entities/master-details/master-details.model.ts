import dayjs from 'dayjs/esm';

export interface IMasterDetails {
  id?: number;
  balance?: number | null;
  totalUnits?: number | null;
  aum?: number | null;
  createdOn?: dayjs.Dayjs | null;
}

export class MasterDetails implements IMasterDetails {
  constructor(
    public id?: number,
    public balance?: number | null,
    public totalUnits?: number | null,
    public aum?: number | null,
    public createdOn?: dayjs.Dayjs | null
  ) {}
}

export function getMasterDetailsIdentifier(masterDetails: IMasterDetails): number | undefined {
  return masterDetails.id;
}
