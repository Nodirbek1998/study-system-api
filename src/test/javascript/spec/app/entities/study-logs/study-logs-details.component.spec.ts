/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import StudyLogsDetailComponent from '@/entities/study-logs/study-logs-details.vue';
import StudyLogsClass from '@/entities/study-logs/study-logs-details.component';
import StudyLogsService from '@/entities/study-logs/study-logs.service';
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
  describe('StudyLogs Management Detail Component', () => {
    let wrapper: Wrapper<StudyLogsClass>;
    let comp: StudyLogsClass;
    let studyLogsServiceStub: SinonStubbedInstance<StudyLogsService>;

    beforeEach(() => {
      studyLogsServiceStub = sinon.createStubInstance<StudyLogsService>(StudyLogsService);

      wrapper = shallowMount<StudyLogsClass>(StudyLogsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { studyLogsService: () => studyLogsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundStudyLogs = { id: 123 };
        studyLogsServiceStub.find.resolves(foundStudyLogs);

        // WHEN
        comp.retrieveStudyLogs(123);
        await comp.$nextTick();

        // THEN
        expect(comp.studyLogs).toBe(foundStudyLogs);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStudyLogs = { id: 123 };
        studyLogsServiceStub.find.resolves(foundStudyLogs);

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
