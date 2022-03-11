/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TestsDetailComponent from '@/entities/tests/tests-details.vue';
import TestsClass from '@/entities/tests/tests-details.component';
import TestsService from '@/entities/tests/tests.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Tests Management Detail Component', () => {
    let wrapper: Wrapper<TestsClass>;
    let comp: TestsClass;
    let testsServiceStub: SinonStubbedInstance<TestsService>;

    beforeEach(() => {
      testsServiceStub = sinon.createStubInstance<TestsService>(TestsService);

      wrapper = shallowMount<TestsClass>(TestsDetailComponent, {
        store,
        localVue,
        router,
        provide: { testsService: () => testsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTests = { id: 123 };
        testsServiceStub.find.resolves(foundTests);

        // WHEN
        comp.retrieveTests(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tests).toBe(foundTests);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTests = { id: 123 };
        testsServiceStub.find.resolves(foundTests);

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
