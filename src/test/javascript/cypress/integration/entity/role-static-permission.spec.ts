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

describe('RoleStaticPermission e2e test', () => {
  const roleStaticPermissionPageUrl = '/role-static-permission';
  const roleStaticPermissionPageUrlPattern = new RegExp('/role-static-permission(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const roleStaticPermissionSample = {};

  let roleStaticPermission: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/role-static-permissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/role-static-permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/role-static-permissions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (roleStaticPermission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/role-static-permissions/${roleStaticPermission.id}`,
      }).then(() => {
        roleStaticPermission = undefined;
      });
    }
  });

  it('RoleStaticPermissions menu should load RoleStaticPermissions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('role-static-permission');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RoleStaticPermission').should('exist');
    cy.url().should('match', roleStaticPermissionPageUrlPattern);
  });

  describe('RoleStaticPermission page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(roleStaticPermissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RoleStaticPermission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/role-static-permission/new$'));
        cy.getEntityCreateUpdateHeading('RoleStaticPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', roleStaticPermissionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/role-static-permissions',
          body: roleStaticPermissionSample,
        }).then(({ body }) => {
          roleStaticPermission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/role-static-permissions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/role-static-permissions?page=0&size=20>; rel="last",<http://localhost/api/role-static-permissions?page=0&size=20>; rel="first"',
              },
              body: [roleStaticPermission],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(roleStaticPermissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RoleStaticPermission page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('roleStaticPermission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', roleStaticPermissionPageUrlPattern);
      });

      it('edit button click should load edit RoleStaticPermission page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoleStaticPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', roleStaticPermissionPageUrlPattern);
      });

      it('last delete button click should delete instance of RoleStaticPermission', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('roleStaticPermission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', roleStaticPermissionPageUrlPattern);

        roleStaticPermission = undefined;
      });
    });
  });

  describe('new RoleStaticPermission page', () => {
    beforeEach(() => {
      cy.visit(`${roleStaticPermissionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RoleStaticPermission');
    });

    it('should create an instance of RoleStaticPermission', () => {
      cy.get(`[data-cy="staticPermission"]`).select('Delete');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        roleStaticPermission = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', roleStaticPermissionPageUrlPattern);
    });
  });
});
