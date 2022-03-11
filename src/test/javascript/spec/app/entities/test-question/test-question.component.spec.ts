/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TestQuestionComponent from '@/entities/test-question/test-question.vue';
import TestQuestionClass from '@/entities/test-question/test-question.component';
import TestQuestionService from '@/entities/test-question/test-question.service';
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
  describe('TestQuestion Management Component', () => {
    let wrapper: Wrapper<TestQuestionClass>;
    let comp: TestQuestionClass;
    let testQuestionServiceStub: SinonStubbedInstance<TestQuestionService>;

    beforeEach(() => {
      testQuestionServiceStub = sinon.createStubInstance<TestQuestionService>(TestQuestionService);
      testQuestionServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TestQuestionClass>(TestQuestionComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          testQuestionService: () => testQuestionServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      testQuestionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTestQuestions();
      await comp.$nextTick();

      // THEN
      expect(testQuestionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.testQuestions[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      testQuestionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(testQuestionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.testQuestions[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      testQuestionServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(testQuestionServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      testQuestionServiceStub.retrieve.reset();
      testQuestionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(testQuestionServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.testQuestions[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      testQuestionServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(testQuestionServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTestQuestion();
      await comp.$nextTick();

      // THEN
      expect(testQuestionServiceStub.delete.called).toBeTruthy();
      expect(testQuestionServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
