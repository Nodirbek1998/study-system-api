/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ArticleUpdateComponent from '@/entities/article/article-update.vue';
import ArticleClass from '@/entities/article/article-update.component';
import ArticleService from '@/entities/article/article.service';

import StudyUsersService from '@/entities/study-users/study-users.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
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
  describe('Article Management Update Component', () => {
    let wrapper: Wrapper<ArticleClass>;
    let comp: ArticleClass;
    let articleServiceStub: SinonStubbedInstance<ArticleService>;

    beforeEach(() => {
      articleServiceStub = sinon.createStubInstance<ArticleService>(ArticleService);

      wrapper = shallowMount<ArticleClass>(ArticleUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          articleService: () => articleServiceStub,
          alertService: () => new AlertService(),

          studyUsersService: () =>
            sinon.createStubInstance<StudyUsersService>(StudyUsersService, {
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
        comp.article = entity;
        articleServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(articleServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.article = entity;
        articleServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(articleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArticle = { id: 123 };
        articleServiceStub.find.resolves(foundArticle);
        articleServiceStub.retrieve.resolves([foundArticle]);

        // WHEN
        comp.beforeRouteEnter({ params: { articleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.article).toBe(foundArticle);
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