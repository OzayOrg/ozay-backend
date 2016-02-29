'use strict';

angular.module('ozayApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('events', {
                parent: 'site',
                url: '/events',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER'],
                    pageTitle: 'Events'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/events/events.html',
                        controller: 'EventsController'
                    }
                },
                resolve: {}
            });
    });
