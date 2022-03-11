/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TestAnswerUpdateComponent from '@/entities/test-answer/test-answer-update.vue';
import TestAnswerClass from '@/entities/test-answer/test-answer-update.component';
import TestAnswerService from '@/entities/test-answer/test-answer.service';

import StudyUsersService from '@/entities/study-users/study-users.service';
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
  describe('TestAnswer Management Update Component', () => {
    let wrapper: Wrapper<TestAnswerClass>;
    let comp: TestAnswerClass;
    let testAnswerServiceStub: SinonStubbedInstance<TestAnswerService>;

    beforeEach(() => {
      testAnswerServiceStub = sinon.createStubInstance<TestAnswerService>(TestAnswerService);

      wrapper = shallowMount<TestAnswerClass>(TestAnswerUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          testAnswerService: () => testAnswerServiceStub,
          alertService: () => new AlertService(),

          studyUsersService: () =>
            sinon.createStubInstance<StudyUsersService>(StudyUsersService, {
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
        comp.testAnswer = entity;
        testAnswerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testAnswerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.testAnswer = entity;
        testAnswerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(testAnswerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTestAnswer = { id: 123 };
        testAnswerServiceStub.find.resolves(foundTestAnswer);
        testAnswerServiceStub.retrieve.resolves([foundTestAnswer]);

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
