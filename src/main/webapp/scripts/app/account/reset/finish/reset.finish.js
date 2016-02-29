'use strict';

angular.module('ozayApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('finishReset', {
                url: '/reset/finish?key',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/reset/finish/reset.finish.html',
                        controller: 'ResetFinishController'
                    }
                },
                resolve: {

                }
            });
    });
