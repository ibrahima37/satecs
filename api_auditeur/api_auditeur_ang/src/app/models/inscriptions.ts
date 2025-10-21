import { Formation } from './formation';
import { Utilisateur } from './utilisateur';
import { Paiement } from './paiement';

export interface Inscriptions{
  id?: number;
  formation?: Formation;
  dateInscription?: string;
  utilisateur?: Utilisateur;
  paiement: Paiement;
  etatInscription?: EtatInscription;
}

export enum EtatInscription {
  VALIDER = 'VALIDER',
  REJETER = 'REJETER',
  ANNULEE = 'ANNULEE',
  EN_COURS_VALIDATION = 'EN_COURS_VALIDATION',
  PAIEMENT_ENaTTENTE = 'PAIEMENT_EN_ATTENTE',
  DOCUMENTS_EN_ATTENTE ='DOCUMENTS_EN_ATTENTE'
}
