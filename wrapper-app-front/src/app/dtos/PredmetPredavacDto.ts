import { AsistentZauzeceDto } from "./AsistentZauzeceDto";

export interface PredmetPredavacDto {
    predmetId: string;
    predmetPlan: number;
    predmetOznaka: string;
    predmetNaziv: string;
    predmetGodina: number;
    profesor: string;
    ostaliProfesori: string[];
    asistentZauzeca: AsistentZauzeceDto[]
    block: boolean;
}