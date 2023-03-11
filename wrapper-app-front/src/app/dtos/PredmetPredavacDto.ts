import { AsistentZauzeceDto } from "./AsistentZauzeceDto";

export interface PredmetPredavacDto {
    predmetOznaka: string;
    predmetNaziv: string;
    predmetGodina: number;
    profesor: string;
    ostaliProfesori: string[];
    asistentiZauzeca: AsistentZauzeceDto[];
}