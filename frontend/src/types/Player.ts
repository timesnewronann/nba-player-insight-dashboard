export type Player = {
  id: number;
  nbaPlayerId: number;
  firstName: string;
  lastName: string;
  fullName: string;
  teamId: number | null;
  position: string | null;
  height: string | null;
  weight: string | null;
  active: boolean | null;
};
