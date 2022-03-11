/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import UnitsDetailComponent from '@/entities/units/units-details.vue';
import UnitsClass from '@/entities/units/units-details.component';
import UnitsService from '@/entities/units/units.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Units Management Detail Component', () => {
    let wrapper: Wrapper<UnitsClass>;
    let comp: UnitsClass;
    let unitsServiceStub: SinonStubbedInstance<UnitsService>;

    beforeEach(() => {
      unitsServiceStub = sinon.createStubInstance<UnitsService>(UnitsService);

      wrapper = shallowMount<UnitsClass>(UnitsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { unitsService: () => unitsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundUnits = { id: 123 };
        unitsServiceStub.find.resolves(foundUnits);

        // WHEN
        comp.retrieveUnits(123);
        await comp.$nextTick();

        // THEN
        expect(comp.units).toBe(foundUnits);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundUnits = { id: 123 };
        unitsServiceStub.find.resolves(foundUnits);

        // WHEN
        comp.beforeRouteEnter({ params: { unitsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.units).toBe(foundUnits);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
