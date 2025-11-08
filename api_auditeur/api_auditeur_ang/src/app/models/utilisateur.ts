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

export interface InscriptionRequest {
  email: string;
  nom: string;
  prenom: string;
  motDePasse: string;
}

export interface ConnexionRequest {
  email: string;
  motDePasse: string;
}

export interface AuthResponse {
  message: string;
  utilisateur: Utilisateur;
  token: string;
}
