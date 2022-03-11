/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TestQuestionDetailComponent from '@/entities/test-question/test-question-details.vue';
import TestQuestionClass from '@/entities/test-question/test-question-details.component';
import TestQuestionService from '@/entities/test-question/test-question.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('TestQuestion Management Detail Component', () => {
    let wrapper: Wrapper<TestQuestionClass>;
    let comp: TestQuestionClass;
    let testQuestionServiceStub: SinonStubbedInstance<TestQuestionService>;

    beforeEach(() => {
      testQuestionServiceStub = sinon.createStubInstance<TestQuestionService>(TestQuestionService);

      wrapper = shallowMount<TestQuestionClass>(TestQuestionDetailComponent, {
        store,
        localVue,
        router,
        provide: { testQuestionService: () => testQuestionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTestQuestion = { id: 123 };
        testQuestionServiceStub.find.resolves(foundTestQuestion);

        // WHEN
        comp.retrieveTestQuestion(123);
        await comp.$nextTick();

        // THEN
        expect(comp.testQuestion).toBe(foundTestQuestion);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTestQuestion = { id: 123 };
        testQuestionServiceStub.find.resolves(foundTestQuestion);

        // WHEN
        comp.beforeRouteEnter({ params: { testQuestionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.testQuestion).toBe(foundTestQuestion);
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
