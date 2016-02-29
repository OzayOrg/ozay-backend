'use strict';

angular.module('ozayApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('organization-user-register', {
                url: '/organization-user-register?key',
                data: {
                    authorities: [],
                    pageTitle: 'Organization User Registration'
                },
                views: {
                    'other@': {
                        templateUrl: 'scripts/app/account/organization-user/organization-user-register.html',
                        controller: 'OrganizationUserRegisterController'
                    }
                },
                resolve: {

                }
            })
            .state('organization-user-register-complete', {
                url: '/organization-user-register-complete',
                data: {
                    authorities: [],
                    pageTitle: 'Organization User Registration'
                },
                views: {
                    'other@': {
                        templateUrl: 'scripts/app/account/organization-user/organization-user-register-complete.html',
                        controller: 'OrganizationUserRegisterCompleteController'
                    }
                },
                resolve: {

                }
            });
    });
