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

describe('StudyUsers e2e test', () => {
  const studyUsersPageUrl = '/study-users';
  const studyUsersPageUrlPattern = new RegExp('/study-users(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const studyUsersSample = {};

  let studyUsers: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/study-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/study-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/study-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (studyUsers) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/study-users/${studyUsers.id}`,
      }).then(() => {
        studyUsers = undefined;
      });
    }
  });

  it('StudyUsers menu should load StudyUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('study-users');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StudyUsers').should('exist');
    cy.url().should('match', studyUsersPageUrlPattern);
  });

  describe('StudyUsers page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(studyUsersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StudyUsers page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/study-users/new$'));
        cy.getEntityCreateUpdateHeading('StudyUsers');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyUsersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/study-users',
          body: studyUsersSample,
        }).then(({ body }) => {
          studyUsers = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/study-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/study-users?page=0&size=20>; rel="last",<http://localhost/api/study-users?page=0&size=20>; rel="first"',
              },
              body: [studyUsers],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(studyUsersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StudyUsers page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('studyUsers');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyUsersPageUrlPattern);
      });

      it('edit button click should load edit StudyUsers page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StudyUsers');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyUsersPageUrlPattern);
      });

      it('last delete button click should delete instance of StudyUsers', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('studyUsers').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', studyUsersPageUrlPattern);

        studyUsers = undefined;
      });
    });
  });

  describe('new StudyUsers page', () => {
    beforeEach(() => {
      cy.visit(`${studyUsersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StudyUsers');
    });

    it('should create an instance of StudyUsers', () => {
      cy.get(`[data-cy="fullName"]`).type('Ergonomic Solutions copy').should('have.value', 'Ergonomic Solutions copy');

      cy.get(`[data-cy="age"]`).type('32350').should('have.value', '32350');

      cy.get(`[data-cy="phone"]`).type('1-340-618-3880 x555').should('have.value', '1-340-618-3880 x555');

      cy.get(`[data-cy="email"]`).type('Larue_Block@gmail.com').should('have.value', 'Larue_Block@gmail.com');

      cy.get(`[data-cy="username"]`).type('Shoes').should('have.value', 'Shoes');

      cy.get(`[data-cy="password"]`).type('Guyana').should('have.value', 'Guyana');

      cy.get(`[data-cy="createdAt"]`).type('2022-03-11').should('have.value', '2022-03-11');

      cy.get(`[data-cy="updatedAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        studyUsers = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', studyUsersPageUrlPattern);
    });
  });
});
