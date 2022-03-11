/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RoleStaticPermissionDetailComponent from '@/entities/role-static-permission/role-static-permission-details.vue';
import RoleStaticPermissionClass from '@/entities/role-static-permission/role-static-permission-details.component';
import RoleStaticPermissionService from '@/entities/role-static-permission/role-static-permission.service';
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
  describe('RoleStaticPermission Management Detail Component', () => {
    let wrapper: Wrapper<RoleStaticPermissionClass>;
    let comp: RoleStaticPermissionClass;
    let roleStaticPermissionServiceStub: SinonStubbedInstance<RoleStaticPermissionService>;

    beforeEach(() => {
      roleStaticPermissionServiceStub = sinon.createStubInstance<RoleStaticPermissionService>(RoleStaticPermissionService);

      wrapper = shallowMount<RoleStaticPermissionClass>(RoleStaticPermissionDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { roleStaticPermissionService: () => roleStaticPermissionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRoleStaticPermission = { id: 123 };
        roleStaticPermissionServiceStub.find.resolves(foundRoleStaticPermission);

        // WHEN
        comp.retrieveRoleStaticPermission(123);
        await comp.$nextTick();

        // THEN
        expect(comp.roleStaticPermission).toBe(foundRoleStaticPermission);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRoleStaticPermission = { id: 123 };
        roleStaticPermissionServiceStub.find.resolves(foundRoleStaticPermission);

        // WHEN
        comp.beforeRouteEnter({ params: { roleStaticPermissionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.roleStaticPermission).toBe(foundRoleStaticPermission);
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
