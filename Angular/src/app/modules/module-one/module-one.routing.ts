import { RouterModule, Routes } from '@angular/router';

// Components
import { ComponentOneComponent } from './component-one/component-one.component';
import { MsalGuard } from '@azure/msal-angular';
import { GroupGuardService } from '../../core/group-guard.service';
import * as config from '../app-config.json';

// Routes
const moduleRoutes: Routes = [
  {
    path: 'room',
    component: ComponentOneComponent,
    canActivate: [MsalGuard, GroupGuardService],
    data: {
      expectedGroup: config.groups.groupAllUsers,
    },
  },
];

export const ModuleOneRouting = RouterModule.forRoot(moduleRoutes);
