'use strict';

angular.module('ozayApp')
    .controller('EventsController', function ($scope, $state, $stateParams, Page, Principal, Events) {
        $scope.pageTitle = 'Events';

		Page.get({state: $state.current.name}).$promise.then(function(data){
        
        });
    });

