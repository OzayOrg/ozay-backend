'use strict';

angular.module('ozayApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('role', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/{buildingId:int}/role',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'ORGANIZATION_HAS_ACCESS', 'ROLE_GET'],
                    pageTitle: 'Role List'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/role/role.html',
                        controller: 'RoleController'
                    }
                },
                resolve: {

                }
            })
            .state('role-edit', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/{buildingId:int}/role/edit/{roleId:int}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'ROLE_PUT', 'ROLE_DELETE'],
                    pageTitle: 'Role Edit'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/role/role.edit.html',
                        controller: 'RoleEditController'
                    }
                },
                resolve: {

                }
            })
            .state('role-new', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/{buildingId:int}/role/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'ROLE_POST'],
                    pageTitle: 'Role New'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/role/role.edit.html',
                        controller: 'RoleEditController'
                    }
                },
                resolve: {

                }
            });
    });
