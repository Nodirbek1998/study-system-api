/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SubjectsDetailComponent from '@/entities/subjects/subjects-details.vue';
import SubjectsClass from '@/entities/subjects/subjects-details.component';
import SubjectsService from '@/entities/subjects/subjects.service';
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
  describe('Subjects Management Detail Component', () => {
    let wrapper: Wrapper<SubjectsClass>;
    let comp: SubjectsClass;
    let subjectsServiceStub: SinonStubbedInstance<SubjectsService>;

    beforeEach(() => {
      subjectsServiceStub = sinon.createStubInstance<SubjectsService>(SubjectsService);

      wrapper = shallowMount<SubjectsClass>(SubjectsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { subjectsService: () => subjectsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSubjects = { id: 123 };
        subjectsServiceStub.find.resolves(foundSubjects);

        // WHEN
        comp.retrieveSubjects(123);
        await comp.$nextTick();

        // THEN
        expect(comp.subjects).toBe(foundSubjects);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubjects = { id: 123 };
        subjectsServiceStub.find.resolves(foundSubjects);

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
