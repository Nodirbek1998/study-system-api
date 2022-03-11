/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ImagesDetailComponent from '@/entities/images/images-details.vue';
import ImagesClass from '@/entities/images/images-details.component';
import ImagesService from '@/entities/images/images.service';
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
  describe('Images Management Detail Component', () => {
    let wrapper: Wrapper<ImagesClass>;
    let comp: ImagesClass;
    let imagesServiceStub: SinonStubbedInstance<ImagesService>;

    beforeEach(() => {
      imagesServiceStub = sinon.createStubInstance<ImagesService>(ImagesService);

      wrapper = shallowMount<ImagesClass>(ImagesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { imagesService: () => imagesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundImages = { id: 123 };
        imagesServiceStub.find.resolves(foundImages);

        // WHEN
        comp.retrieveImages(123);
        await comp.$nextTick();

        // THEN
        expect(comp.images).toBe(foundImages);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundImages = { id: 123 };
        imagesServiceStub.find.resolves(foundImages);

        // WHEN
        comp.beforeRouteEnter({ params: { imagesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.images).toBe(foundImages);
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
