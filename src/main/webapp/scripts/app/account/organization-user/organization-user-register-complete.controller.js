'use strict';

angular.module('ozayApp')
    .controller('OrganizationUserRegisterCompleteController', function ($scope, $state, MessageService) {
        if(MessageService.getSuccessMessage() === undefined){
            $state.go('home');
        } else{
            $scope.ready = true;
        }

    });
