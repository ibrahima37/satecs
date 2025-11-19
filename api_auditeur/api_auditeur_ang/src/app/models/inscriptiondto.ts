import { EtatInscription } from './inscriptions';

export interface InscriptionDto {

  id: number;
  formationId: number;
  utilisateurId: number;
  paiementId: number | null;

  dateInscription: string;
  etatInscription: EtatInscription;

  numeroCni: string;
  dateNaissance: string;
  address: string;
  numeroTel: string;

  fichier: string;
}
