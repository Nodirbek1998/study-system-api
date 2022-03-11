/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TestAnswerDetailComponent from '@/entities/test-answer/test-answer-details.vue';
import TestAnswerClass from '@/entities/test-answer/test-answer-details.component';
import TestAnswerService from '@/entities/test-answer/test-answer.service';
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
  describe('TestAnswer Management Detail Component', () => {
    let wrapper: Wrapper<TestAnswerClass>;
    let comp: TestAnswerClass;
    let testAnswerServiceStub: SinonStubbedInstance<TestAnswerService>;

    beforeEach(() => {
      testAnswerServiceStub = sinon.createStubInstance<TestAnswerService>(TestAnswerService);

      wrapper = shallowMount<TestAnswerClass>(TestAnswerDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { testAnswerService: () => testAnswerServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTestAnswer = { id: 123 };
        testAnswerServiceStub.find.resolves(foundTestAnswer);

        // WHEN
        comp.retrieveTestAnswer(123);
        await comp.$nextTick();

        // THEN
        expect(comp.testAnswer).toBe(foundTestAnswer);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTestAnswer = { id: 123 };
        testAnswerServiceStub.find.resolves(foundTestAnswer);

        // WHEN
        comp.beforeRouteEnter({ params: { testAnswerId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.testAnswer).toBe(foundTestAnswer);
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
