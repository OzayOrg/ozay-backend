'use strict';

angular.module('ozayApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('organization-user', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/{buildingId:int}/organization-user',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER'],
                    pageTitle: 'Organization User List'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/organization-user/organization-user.html',
                        controller: 'OrganizationUserController'
                    }
                },
                resolve: {

                }
            })
            .state('organization-user-edit', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/organization-user/edit/{organizationUserId:int}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'ORGANIZATION-USER_PUT', 'ORGANIZATION-USER_DELETE'],
                    pageTitle: 'Organization User Edit'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/organization-user/organization-user.edit.html',
                        controller: 'OrganizationUserEditController'
                    }
                },
                resolve: {

                }
            })
            .state('organization-user-new', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/organization-user/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'ORGANIZATION-USER_POST'],
                    pageTitle: 'Organization User New'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/organization-user/organization-user.edit.html',
                        controller: 'OrganizationUserEditController'
                    }
                },
                resolve: {

                }
            });
    });
