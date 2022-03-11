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

describe('Groups e2e test', () => {
  const groupsPageUrl = '/groups';
  const groupsPageUrlPattern = new RegExp('/groups(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const groupsSample = {};

  let groups: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (groups) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/groups/${groups.id}`,
      }).then(() => {
        groups = undefined;
      });
    }
  });

  it('Groups menu should load Groups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('groups');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Groups').should('exist');
    cy.url().should('match', groupsPageUrlPattern);
  });

  describe('Groups page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(groupsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Groups page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/groups/new$'));
        cy.getEntityCreateUpdateHeading('Groups');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/groups',
          body: groupsSample,
        }).then(({ body }) => {
          groups = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/groups?page=0&size=20>; rel="last",<http://localhost/api/groups?page=0&size=20>; rel="first"',
              },
              body: [groups],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(groupsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Groups page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('groups');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupsPageUrlPattern);
      });

      it('edit button click should load edit Groups page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Groups');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupsPageUrlPattern);
      });

      it('last delete button click should delete instance of Groups', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('groups').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupsPageUrlPattern);

        groups = undefined;
      });
    });
  });

  describe('new Groups page', () => {
    beforeEach(() => {
      cy.visit(`${groupsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Groups');
    });

    it('should create an instance of Groups', () => {
      cy.get(`[data-cy="name"]`).type('Practical').should('have.value', 'Practical');

      cy.get(`[data-cy="createdAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="updatedAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        groups = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', groupsPageUrlPattern);
    });
  });
});
