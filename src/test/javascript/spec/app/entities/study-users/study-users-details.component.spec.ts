/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import StudyUsersDetailComponent from '@/entities/study-users/study-users-details.vue';
import StudyUsersClass from '@/entities/study-users/study-users-details.component';
import StudyUsersService from '@/entities/study-users/study-users.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('StudyUsers Management Detail Component', () => {
    let wrapper: Wrapper<StudyUsersClass>;
    let comp: StudyUsersClass;
    let studyUsersServiceStub: SinonStubbedInstance<StudyUsersService>;

    beforeEach(() => {
      studyUsersServiceStub = sinon.createStubInstance<StudyUsersService>(StudyUsersService);

      wrapper = shallowMount<StudyUsersClass>(StudyUsersDetailComponent, {
        store,
        localVue,
        router,
        provide: { studyUsersService: () => studyUsersServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundStudyUsers = { id: 123 };
        studyUsersServiceStub.find.resolves(foundStudyUsers);

        // WHEN
        comp.retrieveStudyUsers(123);
        await comp.$nextTick();

        // THEN
        expect(comp.studyUsers).toBe(foundStudyUsers);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStudyUsers = { id: 123 };
        studyUsersServiceStub.find.resolves(foundStudyUsers);

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
