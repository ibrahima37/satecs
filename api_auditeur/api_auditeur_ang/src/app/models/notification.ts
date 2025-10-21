export interface NotificationApp{
  id?: number;
  objet: string;
  contenu: string;
  type: TypeFormation;
  dateEnvoi: string;
  envoyee: boolean;
  destinataire: string;
  canalNotification: CanalNotification;
}

export enum TypeFormation {
  INITIALE = 'INITIALE',
  CONTINUE ='CONTINUE',
  EN_PRESENTIEL = 'EN_PRESENTIEL',
  SPECIALISATION = 'SPECIALISATION',
  EN_LIGNE = 'EN_LIGNE'
}

export enum CanalNotification {
  EMAIL = 'EMAIL',
  SMS = 'SMS',
  PUSH = 'PUSH',
  NOTIFICATION_APP = 'NOTIFICATION_APP'
}
