/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RoleDetailComponent from '@/entities/role/role-details.vue';
import RoleClass from '@/entities/role/role-details.component';
import RoleService from '@/entities/role/role.service';
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
  describe('Role Management Detail Component', () => {
    let wrapper: Wrapper<RoleClass>;
    let comp: RoleClass;
    let roleServiceStub: SinonStubbedInstance<RoleService>;

    beforeEach(() => {
      roleServiceStub = sinon.createStubInstance<RoleService>(RoleService);

      wrapper = shallowMount<RoleClass>(RoleDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { roleService: () => roleServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRole = { id: 123 };
        roleServiceStub.find.resolves(foundRole);

        // WHEN
        comp.retrieveRole(123);
        await comp.$nextTick();

        // THEN
        expect(comp.role).toBe(foundRole);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRole = { id: 123 };
        roleServiceStub.find.resolves(foundRole);

        // WHEN
        comp.beforeRouteEnter({ params: { roleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.role).toBe(foundRole);
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
