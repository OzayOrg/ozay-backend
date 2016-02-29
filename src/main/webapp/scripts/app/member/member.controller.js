'use strict';

angular.module('ozayApp')
    .controller('MemberController', function($scope, $state, MessageService, Page, UserInformation) {
        $scope.pageTitle = 'Directory';
        $scope.memberList = [];

        $scope.predicate = 'unit';

        var message = MessageService.getSuccessMessage();
        if(message !== undefined){
            $scope.successTextAlert = message;
        }

        Page.query({
            state: $state.current.name
        }).$promise.then(function(data) {
            for(var i =0; i< data.length;i++){
                data[i].name = data[i].firstName + " " + data[i].lastName;
                $scope.memberList.push(data[i]);
            }
        });

    });
