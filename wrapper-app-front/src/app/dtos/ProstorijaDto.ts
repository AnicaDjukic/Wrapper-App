import { OrganizacionaJedinicaDto } from "./OrganizacionaJedinicaDto";

export interface ProstorijaDto {
    id: string
    oznaka: string;
    tip: string;
    kapacitet: number;
    orgJedinica: OrganizacionaJedinicaDto[];
    sekundarniTip:string;
}