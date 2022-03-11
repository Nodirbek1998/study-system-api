/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SubjectsUpdateComponent from '@/entities/subjects/subjects-update.vue';
import SubjectsClass from '@/entities/subjects/subjects-update.component';
import SubjectsService from '@/entities/subjects/subjects.service';

import GroupsService from '@/entities/groups/groups.service';
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
  describe('Subjects Management Update Component', () => {
    let wrapper: Wrapper<SubjectsClass>;
    let comp: SubjectsClass;
    let subjectsServiceStub: SinonStubbedInstance<SubjectsService>;

    beforeEach(() => {
      subjectsServiceStub = sinon.createStubInstance<SubjectsService>(SubjectsService);

      wrapper = shallowMount<SubjectsClass>(SubjectsUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          subjectsService: () => subjectsServiceStub,
          alertService: () => new AlertService(),

          groupsService: () =>
            sinon.createStubInstance<GroupsService>(GroupsService, {
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
        comp.subjects = entity;
        subjectsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subjectsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.subjects = entity;
        subjectsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subjectsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubjects = { id: 123 };
        subjectsServiceStub.find.resolves(foundSubjects);
        subjectsServiceStub.retrieve.resolves([foundSubjects]);

        // WHEN
        comp.beforeRouteEnter({ params: { subjectsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.subjects).toBe(foundSubjects);
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
