export interface Location {
  x: number;
  y: number;
  description: string;
}

export interface BookLocation extends Location {
  isbn: number;
  title: string;
  shelfId: string;
}

export interface UserLocation extends Location {
  timestamp: Date;
}