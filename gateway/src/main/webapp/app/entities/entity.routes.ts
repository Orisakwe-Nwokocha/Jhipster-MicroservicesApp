import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'car',
    data: { pageTitle: 'Cars' },
    loadChildren: () => import('./carApp/car/car.routes'),
  },
  {
    path: 'dealer',
    data: { pageTitle: 'Dealers' },
    loadChildren: () => import('./dealerApp/dealer/dealer.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
