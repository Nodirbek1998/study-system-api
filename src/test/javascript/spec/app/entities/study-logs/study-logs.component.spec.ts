/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import StudyLogsComponent from '@/entities/study-logs/study-logs.vue';
import StudyLogsClass from '@/entities/study-logs/study-logs.component';
import StudyLogsService from '@/entities/study-logs/study-logs.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('StudyLogs Management Component', () => {
    let wrapper: Wrapper<StudyLogsClass>;
    let comp: StudyLogsClass;
    let studyLogsServiceStub: SinonStubbedInstance<StudyLogsService>;

    beforeEach(() => {
      studyLogsServiceStub = sinon.createStubInstance<StudyLogsService>(StudyLogsService);
      studyLogsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<StudyLogsClass>(StudyLogsComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          studyLogsService: () => studyLogsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      studyLogsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllStudyLogss();
      await comp.$nextTick();

      // THEN
      expect(studyLogsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.studyLogs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      studyLogsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(studyLogsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.studyLogs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      studyLogsServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(studyLogsServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      studyLogsServiceStub.retrieve.reset();
      studyLogsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(studyLogsServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.studyLogs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      studyLogsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(studyLogsServiceStub.retrieve.callCount).toEqual(1);

      comp.removeStudyLogs();
      await comp.$nextTick();

      // THEN
      expect(studyLogsServiceStub.delete.called).toBeTruthy();
      expect(studyLogsServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
