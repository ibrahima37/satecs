import { Formation } from './formation';
import { Utilisateur } from './utilisateur';
import { Paiement, ModePaiement } from './paiement';

export interface Inscription{
  id: number;
  formation?: Formation;
  dateInscription: string;
  utilisateur?: Utilisateur;
  paiement?: Paiement;
  etatInscription: EtatInscription;
  numeroCni: string;
  dateNaissance: string;
  address: string;
  numeroTel: string;
  fichier: string;

}

export enum EtatInscription {
  VALIDER = 'VALIDER',
  REJETER = 'REJETER',
  ANNULEE = 'ANNULEE',
  EN_COURS_VALIDATION = 'EN_COURS_VALIDATION',
  PAIEMENT_EN_ATTENTE = 'PAIEMENT_EN_ATTENTE',
  DOCUMENTS_EN_ATTENTE ='DOCUMENTS_EN_ATTENTE'
}

export interface CreateInscriptionRequest {

  formationId: number;
  utilisateurId: number;
  paiementId?: number;
  dateInscription: string;
  etatInscription: string;
  numeroCni: string;
  dateNaissance: string;
  address: string;
  numeroTel: string;
  fichier?: File;
}


export interface InscriptionAvecPaiementRequest {
formationId: number;
utilisateurId: number;
numTransaction: string;
modePaiement: ModePaiement;
}
