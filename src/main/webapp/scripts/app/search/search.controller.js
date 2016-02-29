'use strict';

angular.module('ozayApp')
    .controller('SearchController', function ($scope, $state, $stateParams, Page) {
        $scope.pageTitle = 'Search Result';
        Page.get({
            state: $state.current.name,
            keyword:$stateParams.key
        }).$promise.then(function(data) {
            $scope.notifications = data.notifications;
            $scope.members = data.members;
        });


    });
