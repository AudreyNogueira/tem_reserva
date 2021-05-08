export interface DayHour {
    id: number;
    dayOfWeek: DayOfWeekModel;
}

export interface DayOfWeekModel {
    day: string;
    open: string;
    close: string;
}
