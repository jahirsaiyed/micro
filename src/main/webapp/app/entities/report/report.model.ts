import dayjs from 'dayjs/esm';
import { ReportType } from 'app/entities/enumerations/report-type.model';

export interface IReport {
  id?: number;
  type?: ReportType | null;
  balance?: number | null;
  totalUnits?: number | null;
  aum?: number | null;
  createdOn?: dayjs.Dayjs | null;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public type?: ReportType | null,
    public balance?: number | null,
    public totalUnits?: number | null,
    public aum?: number | null,
    public createdOn?: dayjs.Dayjs | null
  ) {}
}

export function getReportIdentifier(report: IReport): number | undefined {
  return report.id;
}
