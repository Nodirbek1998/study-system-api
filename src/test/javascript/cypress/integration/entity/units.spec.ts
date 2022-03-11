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

describe('Units e2e test', () => {
  const unitsPageUrl = '/units';
  const unitsPageUrlPattern = new RegExp('/units(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const unitsSample = {};

  let units: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/units+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/units').as('postEntityRequest');
    cy.intercept('DELETE', '/api/units/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (units) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/units/${units.id}`,
      }).then(() => {
        units = undefined;
      });
    }
  });

  it('Units menu should load Units page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('units');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Units').should('exist');
    cy.url().should('match', unitsPageUrlPattern);
  });

  describe('Units page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(unitsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Units page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/units/new$'));
        cy.getEntityCreateUpdateHeading('Units');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/units',
          body: unitsSample,
        }).then(({ body }) => {
          units = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/units+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/units?page=0&size=20>; rel="last",<http://localhost/api/units?page=0&size=20>; rel="first"',
              },
              body: [units],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(unitsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Units page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('units');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitsPageUrlPattern);
      });

      it('edit button click should load edit Units page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Units');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitsPageUrlPattern);
      });

      it('last delete button click should delete instance of Units', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('units').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitsPageUrlPattern);

        units = undefined;
      });
    });
  });

  describe('new Units page', () => {
    beforeEach(() => {
      cy.visit(`${unitsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Units');
    });

    it('should create an instance of Units', () => {
      cy.get(`[data-cy="nameUz"]`).type('Louisiana').should('have.value', 'Louisiana');

      cy.get(`[data-cy="nameRu"]`).type('Carolina Coordinator').should('have.value', 'Carolina Coordinator');

      cy.get(`[data-cy="nameEn"]`).type('portals Buckinghamshire').should('have.value', 'portals Buckinghamshire');

      cy.get(`[data-cy="createdAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="updatedAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        units = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', unitsPageUrlPattern);
    });
  });
});
