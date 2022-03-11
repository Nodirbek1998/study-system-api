/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import RoleStaticPermissionUpdateComponent from '@/entities/role-static-permission/role-static-permission-update.vue';
import RoleStaticPermissionClass from '@/entities/role-static-permission/role-static-permission-update.component';
import RoleStaticPermissionService from '@/entities/role-static-permission/role-static-permission.service';

import RoleService from '@/entities/role/role.service';
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
  describe('RoleStaticPermission Management Update Component', () => {
    let wrapper: Wrapper<RoleStaticPermissionClass>;
    let comp: RoleStaticPermissionClass;
    let roleStaticPermissionServiceStub: SinonStubbedInstance<RoleStaticPermissionService>;

    beforeEach(() => {
      roleStaticPermissionServiceStub = sinon.createStubInstance<RoleStaticPermissionService>(RoleStaticPermissionService);

      wrapper = shallowMount<RoleStaticPermissionClass>(RoleStaticPermissionUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          roleStaticPermissionService: () => roleStaticPermissionServiceStub,
          alertService: () => new AlertService(),

          roleService: () =>
            sinon.createStubInstance<RoleService>(RoleService, {
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
        comp.roleStaticPermission = entity;
        roleStaticPermissionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(roleStaticPermissionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.roleStaticPermission = entity;
        roleStaticPermissionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(roleStaticPermissionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRoleStaticPermission = { id: 123 };
        roleStaticPermissionServiceStub.find.resolves(foundRoleStaticPermission);
        roleStaticPermissionServiceStub.retrieve.resolves([foundRoleStaticPermission]);

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
