import { Formation } from './formation';
import { Utilisateur } from './utilisateur';
import { Paiement, ModePaiement } from './paiement';

export interface Inscriptions{
  id?: number;
  formation?: Formation;
  dateInscription?: string;
  utilisateur?: Utilisateur;
  paiement: Paiement;
  etatInscription?: EtatInscription;

  formationId: number;
  formationTitre?: string;
  formationDateDebut?: string;
  formationDateFin?: string;
  formationTarif?: number;
  utilisateurId: number;
  utilisateurNom?: string;
  utilisateurPrenom?: string;
  utilisateurEmail?: string;
  paiementId?: number;
  paiementNumPaiement?: string;
  paiementMontant?: number;
  paiementStatut?: string;
}

export enum EtatInscription {
  VALIDER = 'VALIDER',
  REJETER = 'REJETER',
  ANNULEE = 'ANNULEE',
  EN_COURS_VALIDATION = 'EN_COURS_VALIDATION',
  PAIEMENT_ENaTTENTE = 'PAIEMENT_EN_ATTENTE',
  DOCUMENTS_EN_ATTENTE ='DOCUMENTS_EN_ATTENTE'
}

export interface CreateInscriptionRequest {
  prenom: string;
  nom: string;
  tel: string;
  email: string;
  motDePasse: string;
  formationId: number;
  utilisateurId: number;
}


export interface InscriptionAvecPaiementRequest {
formationId: number;
utilisateurId: number;
numTransaction: string;
modePaiement: ModePaiement;
}
