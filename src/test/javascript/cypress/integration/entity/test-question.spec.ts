import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('TestQuestion e2e test', () => {
  const testQuestionPageUrl = '/test-question';
  const testQuestionPageUrlPattern = new RegExp('/test-question(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testQuestionSample = {};

  let testQuestion: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/test-questions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/test-questions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/test-questions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (testQuestion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/test-questions/${testQuestion.id}`,
      }).then(() => {
        testQuestion = undefined;
      });
    }
  });

  it('TestQuestions menu should load TestQuestions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('test-question');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TestQuestion').should('exist');
    cy.url().should('match', testQuestionPageUrlPattern);
  });

  describe('TestQuestion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testQuestionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TestQuestion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/test-question/new$'));
        cy.getEntityCreateUpdateHeading('TestQuestion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testQuestionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/test-questions',
          body: testQuestionSample,
        }).then(({ body }) => {
          testQuestion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/test-questions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/test-questions?page=0&size=20>; rel="last",<http://localhost/api/test-questions?page=0&size=20>; rel="first"',
              },
              body: [testQuestion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(testQuestionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TestQuestion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testQuestion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testQuestionPageUrlPattern);
      });

      it('edit button click should load edit TestQuestion page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestQuestion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testQuestionPageUrlPattern);
      });

      it('last delete button click should delete instance of TestQuestion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('testQuestion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testQuestionPageUrlPattern);

        testQuestion = undefined;
      });
    });
  });

  describe('new TestQuestion page', () => {
    beforeEach(() => {
      cy.visit(`${testQuestionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TestQuestion');
    });

    it('should create an instance of TestQuestion', () => {
      cy.get(`[data-cy="name"]`).type('portals').should('have.value', 'portals');

      cy.get(`[data-cy="level"]`).type('39684').should('have.value', '39684');

      cy.get(`[data-cy="answerA"]`).should('not.be.checked');
      cy.get(`[data-cy="answerA"]`).click().should('be.checked');

      cy.get(`[data-cy="answerB"]`).should('not.be.checked');
      cy.get(`[data-cy="answerB"]`).click().should('be.checked');

      cy.get(`[data-cy="answerC"]`).should('not.be.checked');
      cy.get(`[data-cy="answerC"]`).click().should('be.checked');

      cy.get(`[data-cy="answerD"]`).should('not.be.checked');
      cy.get(`[data-cy="answerD"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        testQuestion = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', testQuestionPageUrlPattern);
    });
  });
});
