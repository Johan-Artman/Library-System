export interface Member {
  id: number;
  firstName: string;
  lastName: string;
  memberType: number;
  socialSecNr: string;
}

export interface MemberRequest {
  firstname: string;
  lastname: string;
  memberType: MemberType;
  socSecNr: string;
}

export enum MemberType {
  UNDERGRADUATE = 'UNDERGRADUATE',
  POSTGRADUATE = 'POSTGRADUATE', 
  PHD = 'PHD',
  TEACHER = 'TEACHER'
}