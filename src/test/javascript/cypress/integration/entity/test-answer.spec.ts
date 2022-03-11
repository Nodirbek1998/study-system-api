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

describe('TestAnswer e2e test', () => {
  const testAnswerPageUrl = '/test-answer';
  const testAnswerPageUrlPattern = new RegExp('/test-answer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testAnswerSample = {};

  let testAnswer: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/test-answers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/test-answers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/test-answers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (testAnswer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/test-answers/${testAnswer.id}`,
      }).then(() => {
        testAnswer = undefined;
      });
    }
  });

  it('TestAnswers menu should load TestAnswers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('test-answer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TestAnswer').should('exist');
    cy.url().should('match', testAnswerPageUrlPattern);
  });

  describe('TestAnswer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testAnswerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TestAnswer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/test-answer/new$'));
        cy.getEntityCreateUpdateHeading('TestAnswer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testAnswerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/test-answers',
          body: testAnswerSample,
        }).then(({ body }) => {
          testAnswer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/test-answers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/test-answers?page=0&size=20>; rel="last",<http://localhost/api/test-answers?page=0&size=20>; rel="first"',
              },
              body: [testAnswer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(testAnswerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TestAnswer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testAnswer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testAnswerPageUrlPattern);
      });

      it('edit button click should load edit TestAnswer page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestAnswer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testAnswerPageUrlPattern);
      });

      it('last delete button click should delete instance of TestAnswer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('testAnswer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testAnswerPageUrlPattern);

        testAnswer = undefined;
      });
    });
  });

  describe('new TestAnswer page', () => {
    beforeEach(() => {
      cy.visit(`${testAnswerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TestAnswer');
    });

    it('should create an instance of TestAnswer', () => {
      cy.get(`[data-cy="createdAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="updatedAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="right"]`).should('not.be.checked');
      cy.get(`[data-cy="right"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        testAnswer = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', testAnswerPageUrlPattern);
    });
  });
});
