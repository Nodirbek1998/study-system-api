/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TaskAnswerUpdateComponent from '@/entities/task-answer/task-answer-update.vue';
import TaskAnswerClass from '@/entities/task-answer/task-answer-update.component';
import TaskAnswerService from '@/entities/task-answer/task-answer.service';

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
  describe('TaskAnswer Management Update Component', () => {
    let wrapper: Wrapper<TaskAnswerClass>;
    let comp: TaskAnswerClass;
    let taskAnswerServiceStub: SinonStubbedInstance<TaskAnswerService>;

    beforeEach(() => {
      taskAnswerServiceStub = sinon.createStubInstance<TaskAnswerService>(TaskAnswerService);

      wrapper = shallowMount<TaskAnswerClass>(TaskAnswerUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          taskAnswerService: () => taskAnswerServiceStub,
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
        comp.taskAnswer = entity;
        taskAnswerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(taskAnswerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.taskAnswer = entity;
        taskAnswerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(taskAnswerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTaskAnswer = { id: 123 };
        taskAnswerServiceStub.find.resolves(foundTaskAnswer);
        taskAnswerServiceStub.retrieve.resolves([foundTaskAnswer]);

        // WHEN
        comp.beforeRouteEnter({ params: { taskAnswerId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.taskAnswer).toBe(foundTaskAnswer);
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
