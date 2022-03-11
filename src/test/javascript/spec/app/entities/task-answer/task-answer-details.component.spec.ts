/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TaskAnswerDetailComponent from '@/entities/task-answer/task-answer-details.vue';
import TaskAnswerClass from '@/entities/task-answer/task-answer-details.component';
import TaskAnswerService from '@/entities/task-answer/task-answer.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('TaskAnswer Management Detail Component', () => {
    let wrapper: Wrapper<TaskAnswerClass>;
    let comp: TaskAnswerClass;
    let taskAnswerServiceStub: SinonStubbedInstance<TaskAnswerService>;

    beforeEach(() => {
      taskAnswerServiceStub = sinon.createStubInstance<TaskAnswerService>(TaskAnswerService);

      wrapper = shallowMount<TaskAnswerClass>(TaskAnswerDetailComponent, {
        store,
        localVue,
        router,
        provide: { taskAnswerService: () => taskAnswerServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTaskAnswer = { id: 123 };
        taskAnswerServiceStub.find.resolves(foundTaskAnswer);

        // WHEN
        comp.retrieveTaskAnswer(123);
        await comp.$nextTick();

        // THEN
        expect(comp.taskAnswer).toBe(foundTaskAnswer);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTaskAnswer = { id: 123 };
        taskAnswerServiceStub.find.resolves(foundTaskAnswer);

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
