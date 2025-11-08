import { Routes } from '@angular/router';
import { Formations } from "./ui/formations/formations";
import { Inscriptions } from "./ui/inscriptions/inscriptions";
import { Contacts } from "./ui/contacts/contacts";
import { Login } from "./ui/login/login";
import { Notifications } from "./ui/notifications/notifications";
import { NouvelleFormation } from "./ui/nouvelle-formation/nouvelle-formation";
import { Paiements } from "./ui/paiements/paiements";
import { Dashboard } from "./ui/dashboard/dashboard";
import { FormationDetail } from "./ui/formation-detail/formation-detail";
import { Home } from "./ui/home/home";
import { AdminTemplate } from "./ui/admin-template/admin-template";
import { AuthGuard } from './services/authguard.service';
import { AuthorizationGuard } from './services/authorizationguard';

export const routes: Routes = [
  { path : "", component : Login},
  { path : "login", component : Login},
  { path : "admin", component : AdminTemplate,
    canActivate : [ AuthGuard ],
    children : [
      { path: "home", component: Home },
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: "dashboard", component: Dashboard },
      { path : "formations", component : Formations},
      { path : "inscription", component : Inscriptions},
      { path: "notification", component: Notifications },
      { path: "formations/nouvelle", component: NouvelleFormation },
      {
        path : "paiements", component : Paiements,
        canActivate : [ AuthorizationGuard ], data: {roles : ['ADMIN']}
      },
      { path : "contacts", component : Contacts},
      { path: "formations/:id", component: FormationDetail },
    ]
  },
];


