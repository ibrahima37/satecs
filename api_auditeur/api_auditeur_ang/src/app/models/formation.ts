import { TypeFormation } from './notification';

export interface Formation {
  id?: number;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  capacite: number;
  tarif: number;
  typeFormation: TypeFormation;
  niveauFormation: NiveauFormation;
  dateCreation?: string;
  dateModification?: string;
}

export enum NiveauFormation{
  DEBUTANT = 'DEBUTANT',
  INTERMEDIAIRE = 'INTERMEDIAIRE',
  AVANCE = 'AVANCE',
  EXPERT = 'EXPERT'
}
