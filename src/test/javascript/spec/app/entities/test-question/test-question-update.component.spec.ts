/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TestQuestionUpdateComponent from '@/entities/test-question/test-question-update.vue';
import TestQuestionClass from '@/entities/test-question/test-question-update.component';
import TestQuestionService from '@/entities/test-question/test-question.service';

import TestsService from '@/entities/tests/tests.service';
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
  describe('TestQuestion Management Update Component', () => {
    let wrapper: Wrapper<TestQuestionClass>;
    let comp: TestQuestionClass;
    let testQuestionServiceStub: SinonStubbedInstance<TestQuestionService>;

    beforeEach(() => {
      testQuestionServiceStub = sinon.createStubInstance<TestQuestionService>(TestQuestionService);

      wrapper = shallowMount<TestQuestionClass>(TestQuestionUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          testQuestionService: () => testQuestionServiceStub,
          alertService: () => new AlertService(),

          testsService: () =>
            sinon.createStubInstance<TestsService>(TestsService, {
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
        comp.testQuestion = entity;
        testQuestionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testQuestionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.testQuestion = entity;
        testQuestionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testQuestionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTestQuestion = { id: 123 };
        testQuestionServiceStub.find.resolves(foundTestQuestion);
        testQuestionServiceStub.retrieve.resolves([foundTestQuestion]);

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
