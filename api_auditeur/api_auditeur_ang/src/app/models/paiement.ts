export interface Paiement{
  id?: number;
  numPaiemet: string;
  numTransacton: string;
  montant: number;
  statutpPaiement: StatutPaiement;
  datePaiement: string;
  modePaiement: ModePaiement;
}

export enum StatutPaiement{
  EN_ATTENTE = 'EN_ATTENTE',
  VALIDE = 'VALIDE',
  REJETER = 'REJETER',
  REMBOURSE = 'REMBOURSE',
  ANNULER ='ANNULER'
}

export enum ModePaiement{
  CARTE_BANCAIRE = 'CARTE_BANCAIRE',
  VIREMENT = 'VIREMENT',
  ESPECES = 'ESPECES',
  MOBILE_MONEY = 'MOBILE_MONEY',
  CHEQUE = 'CHEQUE'
}
