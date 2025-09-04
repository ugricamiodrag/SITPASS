
export interface Facility {
    id?: number;
    name: string;
    description: string;
    createdAt: Date;
    address: string;
    city: string;
    totalRating: number;
    active: boolean;
    workDays: WorkDay[];
    disciplines: Discipline[];
    images?: string[];



  }





  export interface WorkDay {
    id?: number;
    validFrom: Date;
    day: dayOfWeek;
    from: string;
    until: string;
  }
  export enum dayOfWeek {
    MONDAY = 'MONDAY',
    TUESDAY = 'TUESDAY',
    WEDNESDAY = 'WEDNESDAY',
    THURSDAY = 'THURSDAY',
    FRIDAY = 'FRIDAY',
    SATURDAY = 'SATURDAY',
    SUNDAY = 'SUNDAY'
}


  export interface Discipline {
    id?: number;
    name: string;
  }



