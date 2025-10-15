import { Routes } from '@angular/router';
import { Formations } from "./ui/formations/formations";
import { Inscriptions } from "./ui/inscriptions/inscriptions";
import { Home } from "./ui/home/home";

export const routes: Routes = [
  { path: '', component: Home },
  {path : "formations", component : Formations},
  {path : "inscriptions", component : Inscriptions},
  { path: '', redirectTo: 'formations', pathMatch: 'full' },
  ];


