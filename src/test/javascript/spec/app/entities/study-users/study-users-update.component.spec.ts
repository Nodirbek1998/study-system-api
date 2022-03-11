/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import StudyUsersUpdateComponent from '@/entities/study-users/study-users-update.vue';
import StudyUsersClass from '@/entities/study-users/study-users-update.component';
import StudyUsersService from '@/entities/study-users/study-users.service';

import RoleService from '@/entities/role/role.service';

import GroupsService from '@/entities/groups/groups.service';

import TestAnswerService from '@/entities/test-answer/test-answer.service';

import TaskAnswerService from '@/entities/task-answer/task-answer.service';
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
  describe('StudyUsers Management Update Component', () => {
    let wrapper: Wrapper<StudyUsersClass>;
    let comp: StudyUsersClass;
    let studyUsersServiceStub: SinonStubbedInstance<StudyUsersService>;

    beforeEach(() => {
      studyUsersServiceStub = sinon.createStubInstance<StudyUsersService>(StudyUsersService);

      wrapper = shallowMount<StudyUsersClass>(StudyUsersUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          studyUsersService: () => studyUsersServiceStub,
          alertService: () => new AlertService(),

          roleService: () =>
            sinon.createStubInstance<RoleService>(RoleService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          groupsService: () =>
            sinon.createStubInstance<GroupsService>(GroupsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          testAnswerService: () =>
            sinon.createStubInstance<TestAnswerService>(TestAnswerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          taskAnswerService: () =>
            sinon.createStubInstance<TaskAnswerService>(TaskAnswerService, {
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
        comp.studyUsers = entity;
        studyUsersServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(studyUsersServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.studyUsers = entity;
        studyUsersServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(studyUsersServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStudyUsers = { id: 123 };
        studyUsersServiceStub.find.resolves(foundStudyUsers);
        studyUsersServiceStub.retrieve.resolves([foundStudyUsers]);

        // WHEN
        comp.beforeRouteEnter({ params: { studyUsersId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.studyUsers).toBe(foundStudyUsers);
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
