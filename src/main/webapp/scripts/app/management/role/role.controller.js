'use strict';

angular.module('ozayApp')
    .controller('RoleController', function($scope, $state, $stateParams, Page, Role, Auth, MessageService, UserInformation) {
        if(UserInformation.getOrganizationId() != $stateParams.organizationId){
            Auth.authorize(true).then(function(){
                $state.reload();
            });
        }
        $scope.pageTitle = 'Role List';
        $scope.contentTitle = 'Role list';
        $scope.roles = [];
        $scope.organizationId = $stateParams.organizationId;
        $scope.buildingId = $stateParams.buildingId;
        $scope.button = true;

        var message = MessageService.getSuccessMessage();
        if(message !== undefined){
            $scope.successTextAlert = message;
        }

        Page.get({
            state: $state.current.name,
            building: $stateParams.buildingId
        }).$promise.then(function(data) {
            $scope.roles = data.roles;
        });

    });
