export interface Lending {
  lendingId: number;
  memberID: number;
  isbn: string;
  lendingDate: string;
  dueDate: string;
  returnDate?: string;
  isReturned: boolean;
}