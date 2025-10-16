export interface Formation {
  id?: number,
  titre: string;
  description: string;
  dateDebut: string;
  deteFin: string;
  capacite: number;
  tarif: number;
  typeFormation: string;
  niveauFormation: string;
  dateCreation?: Date;
  dateModification?: Date;
}
