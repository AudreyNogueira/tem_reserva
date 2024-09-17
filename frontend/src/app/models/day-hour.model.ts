export interface DayHour {
    id: number;
    dayOfWeek: DayOfWeekModel;
}

export interface DayOfWeekModel {
    day: string;
    openingTime: string;
    closingTime: string;
}
