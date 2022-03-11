/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import StudyLogsUpdateComponent from '@/entities/study-logs/study-logs-update.vue';
import StudyLogsClass from '@/entities/study-logs/study-logs-update.component';
import StudyLogsService from '@/entities/study-logs/study-logs.service';

import StudyUsersService from '@/entities/study-users/study-users.service';
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
  describe('StudyLogs Management Update Component', () => {
    let wrapper: Wrapper<StudyLogsClass>;
    let comp: StudyLogsClass;
    let studyLogsServiceStub: SinonStubbedInstance<StudyLogsService>;

    beforeEach(() => {
      studyLogsServiceStub = sinon.createStubInstance<StudyLogsService>(StudyLogsService);

      wrapper = shallowMount<StudyLogsClass>(StudyLogsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          studyLogsService: () => studyLogsServiceStub,
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
        comp.studyLogs = entity;
        studyLogsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(studyLogsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.studyLogs = entity;
        studyLogsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(studyLogsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStudyLogs = { id: 123 };
        studyLogsServiceStub.find.resolves(foundStudyLogs);
        studyLogsServiceStub.retrieve.resolves([foundStudyLogs]);

        // WHEN
        comp.beforeRouteEnter({ params: { studyLogsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.studyLogs).toBe(foundStudyLogs);
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
