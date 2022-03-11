/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import UnitsUpdateComponent from '@/entities/units/units-update.vue';
import UnitsClass from '@/entities/units/units-update.component';
import UnitsService from '@/entities/units/units.service';

import SubjectsService from '@/entities/subjects/subjects.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Units Management Update Component', () => {
    let wrapper: Wrapper<UnitsClass>;
    let comp: UnitsClass;
    let unitsServiceStub: SinonStubbedInstance<UnitsService>;

    beforeEach(() => {
      unitsServiceStub = sinon.createStubInstance<UnitsService>(UnitsService);

      wrapper = shallowMount<UnitsClass>(UnitsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          unitsService: () => unitsServiceStub,
          alertService: () => new AlertService(),

          subjectsService: () =>
            sinon.createStubInstance<SubjectsService>(SubjectsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.units = entity;
        unitsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.units = entity;
        unitsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundUnits = { id: 123 };
        unitsServiceStub.find.resolves(foundUnits);
        unitsServiceStub.retrieve.resolves([foundUnits]);

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
