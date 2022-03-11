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

describe('Tests e2e test', () => {
  const testsPageUrl = '/tests';
  const testsPageUrlPattern = new RegExp('/tests(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testsSample = {};

  let tests: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tests) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tests/${tests.id}`,
      }).then(() => {
        tests = undefined;
      });
    }
  });

  it('Tests menu should load Tests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tests');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tests').should('exist');
    cy.url().should('match', testsPageUrlPattern);
  });

  describe('Tests page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tests page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tests/new$'));
        cy.getEntityCreateUpdateHeading('Tests');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tests',
          body: testsSample,
        }).then(({ body }) => {
          tests = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tests?page=0&size=20>; rel="last",<http://localhost/api/tests?page=0&size=20>; rel="first"',
              },
              body: [tests],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(testsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tests page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tests');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testsPageUrlPattern);
      });

      it('edit button click should load edit Tests page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tests');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testsPageUrlPattern);
      });

      it('last delete button click should delete instance of Tests', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tests').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', testsPageUrlPattern);

        tests = undefined;
      });
    });
  });

  describe('new Tests page', () => {
    beforeEach(() => {
      cy.visit(`${testsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tests');
    });

    it('should create an instance of Tests', () => {
      cy.get(`[data-cy="name"]`).type('models Games').should('have.value', 'models Games');

      cy.get(`[data-cy="status"]`).select('Oraliq');

      cy.get(`[data-cy="deadline"]`).type('2022-03-11').should('have.value', '2022-03-11');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tests = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', testsPageUrlPattern);
    });
  });
});
