'use strict';

angular.module('ozayApp')
    .controller('PageController', function ($scope, MenuSearchState) {
        $scope.button_state = MenuSearchState;

        $scope.navClicked = function(){
            $scope.loaded = true;
        }

    });
