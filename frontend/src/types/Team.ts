export type Team = {
  id: number;
  nbaTeamId: number;
  teamName: string;
  abbreviation: string;
  city: string;
  conference: string | null;
  division: string | null;
};
