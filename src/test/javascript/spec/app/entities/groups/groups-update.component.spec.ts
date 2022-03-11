/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import GroupsUpdateComponent from '@/entities/groups/groups-update.vue';
import GroupsClass from '@/entities/groups/groups-update.component';
import GroupsService from '@/entities/groups/groups.service';

import StudyUsersService from '@/entities/study-users/study-users.service';

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
  describe('Groups Management Update Component', () => {
    let wrapper: Wrapper<GroupsClass>;
    let comp: GroupsClass;
    let groupsServiceStub: SinonStubbedInstance<GroupsService>;

    beforeEach(() => {
      groupsServiceStub = sinon.createStubInstance<GroupsService>(GroupsService);

      wrapper = shallowMount<GroupsClass>(GroupsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          groupsService: () => groupsServiceStub,
          alertService: () => new AlertService(),

          studyUsersService: () =>
            sinon.createStubInstance<StudyUsersService>(StudyUsersService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

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
        comp.groups = entity;
        groupsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(groupsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.groups = entity;
        groupsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(groupsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGroups = { id: 123 };
        groupsServiceStub.find.resolves(foundGroups);
        groupsServiceStub.retrieve.resolves([foundGroups]);

        // WHEN
        comp.beforeRouteEnter({ params: { groupsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.groups).toBe(foundGroups);
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
