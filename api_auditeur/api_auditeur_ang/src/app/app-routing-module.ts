import { Routes } from '@angular/router';
import { Formations } from "./ui/formations/formations";
import { Inscriptions } from "./ui/inscriptions/inscriptions";
import { Contacts } from "./ui/contacts/contacts";
import { FormationDetail } from "./ui/formation-detail/formation-detail";
import { Home } from "./ui/home/home";

export const routes: Routes = [
  { path: '', component: Home },
  {path : "formations", component : Formations},
  {path : "inscriptions", component : Inscriptions},
  {path : "contacts", component : Contacts},
  { path: 'formations/:id', component: FormationDetail },
  { path: 'login', loadComponent: () => import('./ui/login/login').then(m => m.Login),
    pathMatch: 'full' },
  {
    path: 'register',
    loadComponent: () => import('./ui/register/register').then(m => m.Register)
  }

  ];


