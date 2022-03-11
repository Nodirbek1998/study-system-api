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

describe('TaskAnswer e2e test', () => {
  const taskAnswerPageUrl = '/task-answer';
  const taskAnswerPageUrlPattern = new RegExp('/task-answer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const taskAnswerSample = {};

  let taskAnswer: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/task-answers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/task-answers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/task-answers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taskAnswer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/task-answers/${taskAnswer.id}`,
      }).then(() => {
        taskAnswer = undefined;
      });
    }
  });

  it('TaskAnswers menu should load TaskAnswers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('task-answer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaskAnswer').should('exist');
    cy.url().should('match', taskAnswerPageUrlPattern);
  });

  describe('TaskAnswer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taskAnswerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaskAnswer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/task-answer/new$'));
        cy.getEntityCreateUpdateHeading('TaskAnswer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taskAnswerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/task-answers',
          body: taskAnswerSample,
        }).then(({ body }) => {
          taskAnswer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/task-answers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/task-answers?page=0&size=20>; rel="last",<http://localhost/api/task-answers?page=0&size=20>; rel="first"',
              },
              body: [taskAnswer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(taskAnswerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaskAnswer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taskAnswer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taskAnswerPageUrlPattern);
      });

      it('edit button click should load edit TaskAnswer page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaskAnswer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taskAnswerPageUrlPattern);
      });

      it('last delete button click should delete instance of TaskAnswer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taskAnswer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taskAnswerPageUrlPattern);

        taskAnswer = undefined;
      });
    });
  });

  describe('new TaskAnswer page', () => {
    beforeEach(() => {
      cy.visit(`${taskAnswerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaskAnswer');
    });

    it('should create an instance of TaskAnswer', () => {
      cy.get(`[data-cy="createdAt"]`).type('2022-03-10').should('have.value', '2022-03-10');

      cy.get(`[data-cy="updatedAt"]`).type('2022-03-11').should('have.value', '2022-03-11');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        taskAnswer = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', taskAnswerPageUrlPattern);
    });
  });
});
