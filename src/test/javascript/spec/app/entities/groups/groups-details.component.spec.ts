/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import GroupsDetailComponent from '@/entities/groups/groups-details.vue';
import GroupsClass from '@/entities/groups/groups-details.component';
import GroupsService from '@/entities/groups/groups.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Groups Management Detail Component', () => {
    let wrapper: Wrapper<GroupsClass>;
    let comp: GroupsClass;
    let groupsServiceStub: SinonStubbedInstance<GroupsService>;

    beforeEach(() => {
      groupsServiceStub = sinon.createStubInstance<GroupsService>(GroupsService);

      wrapper = shallowMount<GroupsClass>(GroupsDetailComponent, {
        store,
        localVue,
        router,
        provide: { groupsService: () => groupsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundGroups = { id: 123 };
        groupsServiceStub.find.resolves(foundGroups);

        // WHEN
        comp.retrieveGroups(123);
        await comp.$nextTick();

        // THEN
        expect(comp.groups).toBe(foundGroups);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundGroups = { id: 123 };
        groupsServiceStub.find.resolves(foundGroups);

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
