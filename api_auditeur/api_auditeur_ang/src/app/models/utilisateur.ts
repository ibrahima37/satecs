import { Inscriptions } from './inscriptions';

export interface Utilisateur{
  id?: number;
  email: string;
  nom: string;
  prenom: string;
  motDePasse?: string;
  role: Role;
  inscriptionList?: Inscriptions[];
}

export enum Role {
  ADMIN = 'ADMIN',
  AUDITEUR = 'AUDITEUR',
  SUPER_ADMIN = 'SUPER_ADMIN'
}
