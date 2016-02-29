'use strict';

angular.module('ozayApp')
    .controller('MemberRegisterController', function ($scope, $state, $timeout, $stateParams, InvitedMember, MessageService) {
        if($stateParams.key == undefined){
            $state.go('error');
        }
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {};


        InvitedMember.get({
            key: $stateParams.key
        }).$promise.then(function(data) {
            $scope.building = data.building;
            $scope.success = true;
        }, function(error){
            if (error.status === 400 && error.data.message === 'Sorry, the record could not be found') {
                $scope.recordNotExist = 'ERROR';
            }
            if (error.status === 400 && error.data.message === 'You are already activated') {
                $scope.recordExist = 'ERROR';
            }
        });

        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey =  'en' ;
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.recordExist = null;
                $scope.recordNotExist = null;

                InvitedMember.save({key:$stateParams.key}, $scope.registerAccount, function (data) {
                    MessageService.setSuccessMessage('Registration completed');
                    $state.go('member-register-complete');
                }, function (error){

                    if (error.status === 400 && error.data.message === 'login already in use') {
                        $scope.errorUserExists = 'ERROR';
                    }
                    else if (error.status === 400) {
                         $scope.recordExist = 'ERROR';
                    }
                    else {
                        $scope.success = null;
                        $scope.error = 'ERROR';
                    }

                }).$promise.finally(function(){

                });
            }
        };
    });
