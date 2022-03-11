/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TestsUpdateComponent from '@/entities/tests/tests-update.vue';
import TestsClass from '@/entities/tests/tests-update.component';
import TestsService from '@/entities/tests/tests.service';

import SubjectsService from '@/entities/subjects/subjects.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
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
  describe('Tests Management Update Component', () => {
    let wrapper: Wrapper<TestsClass>;
    let comp: TestsClass;
    let testsServiceStub: SinonStubbedInstance<TestsService>;

    beforeEach(() => {
      testsServiceStub = sinon.createStubInstance<TestsService>(TestsService);

      wrapper = shallowMount<TestsClass>(TestsUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          testsService: () => testsServiceStub,
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
        comp.tests = entity;
        testsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.tests = entity;
        testsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTests = { id: 123 };
        testsServiceStub.find.resolves(foundTests);
        testsServiceStub.retrieve.resolves([foundTests]);

        // WHEN
        comp.beforeRouteEnter({ params: { testsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tests).toBe(foundTests);
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
