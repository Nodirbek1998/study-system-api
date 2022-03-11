/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ArticleDetailComponent from '@/entities/article/article-details.vue';
import ArticleClass from '@/entities/article/article-details.component';
import ArticleService from '@/entities/article/article.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Article Management Detail Component', () => {
    let wrapper: Wrapper<ArticleClass>;
    let comp: ArticleClass;
    let articleServiceStub: SinonStubbedInstance<ArticleService>;

    beforeEach(() => {
      articleServiceStub = sinon.createStubInstance<ArticleService>(ArticleService);

      wrapper = shallowMount<ArticleClass>(ArticleDetailComponent, {
        store,
        localVue,
        router,
        provide: { articleService: () => articleServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundArticle = { id: 123 };
        articleServiceStub.find.resolves(foundArticle);

        // WHEN
        comp.retrieveArticle(123);
        await comp.$nextTick();

        // THEN
        expect(comp.article).toBe(foundArticle);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArticle = { id: 123 };
        articleServiceStub.find.resolves(foundArticle);

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
