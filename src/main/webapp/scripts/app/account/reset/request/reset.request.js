'use strict';

angular.module('ozayApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('requestReset', {

                url: '/reset/request',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/reset/request/reset.request.html',
                        controller: 'RequestResetController'
                    }
                },
                resolve: {

                }
            });
    });
