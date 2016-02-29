'use strict';

angular.module('ozayApp')
    .controller('MainController', function ($scope, UserInformation) {
        $scope.pageTitle = 'Dashboard';
        if(UserInformation.getBuilding() !== undefined){
            $scope.selectedBuildingName = "to " + UserInformation.getBuilding().name;
        }



    });
