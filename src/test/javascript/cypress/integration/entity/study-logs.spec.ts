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

describe('StudyLogs e2e test', () => {
  const studyLogsPageUrl = '/study-logs';
  const studyLogsPageUrlPattern = new RegExp('/study-logs(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const studyLogsSample = {};

  let studyLogs: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/study-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/study-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/study-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (studyLogs) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/study-logs/${studyLogs.id}`,
      }).then(() => {
        studyLogs = undefined;
      });
    }
  });

  it('StudyLogs menu should load StudyLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('study-logs');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StudyLogs').should('exist');
    cy.url().should('match', studyLogsPageUrlPattern);
  });

  describe('StudyLogs page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(studyLogsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StudyLogs page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/study-logs/new$'));
        cy.getEntityCreateUpdateHeading('StudyLogs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyLogsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/study-logs',
          body: studyLogsSample,
        }).then(({ body }) => {
          studyLogs = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/study-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/study-logs?page=0&size=20>; rel="last",<http://localhost/api/study-logs?page=0&size=20>; rel="first"',
              },
              body: [studyLogs],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(studyLogsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StudyLogs page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('studyLogs');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyLogsPageUrlPattern);
      });

      it('edit button click should load edit StudyLogs page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StudyLogs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyLogsPageUrlPattern);
      });

      it('last delete button click should delete instance of StudyLogs', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('studyLogs').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyLogsPageUrlPattern);

        studyLogs = undefined;
      });
    });
  });

  describe('new StudyLogs page', () => {
    beforeEach(() => {
      cy.visit(`${studyLogsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StudyLogs');
    });

    it('should create an instance of StudyLogs', () => {
      cy.get(`[data-cy="operationName"]`).type('1080p synthesizing').should('have.value', '1080p synthesizing');

      cy.get(`[data-cy="clientIp"]`).type('circuit THX').should('have.value', 'circuit THX');

      cy.get(`[data-cy="host"]`).type('Account Plastic').should('have.value', 'Account Plastic');

      cy.get(`[data-cy="createdAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="actionType"]`).select('Logout');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        studyLogs = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', studyLogsPageUrlPattern);
    });
  });
});
