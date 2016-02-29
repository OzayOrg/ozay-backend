'use strict';

angular.module('ozayApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('building-edit', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/{buildingId:int}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'BUILDING_PUT'],
                    pageTitle: 'Building Edit'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/building/building.edit.html',
                        controller: 'BuildingEditController'
                    }
                },
                resolve: {

                }
            })
            .state('building-new', {
                parent: 'manage',
                url: '/management/organization/{organizationId:int}/building/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'BUILDING_POST'],
                    pageTitle: 'Building New'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/management/building/building.edit.html',
                        controller: 'BuildingEditController'
                    }
                },
                resolve: {

                }
            });
    });
