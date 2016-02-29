'use strict';

angular.module('ozayApp')
    .controller('OrganizationUserRegisterController', function ($scope, $state, $timeout, $stateParams, OrganizationUser, MessageService) {
        if($stateParams.key == undefined){
            $state.go('error');
        }
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {};
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey =  'en' ;
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;

                OrganizationUser.save({method:'register', key:$stateParams.key}, $scope.registerAccount, function (data) {
                    MessageService.setSuccessMessage('Registration completed');
                    $state.go('organization-user-register-complete');
                }, function (error){
                    $scope.success = null;
                    if (error.status === 400 && error.data.message === 'login already in use') {
                        $scope.errorUserExists = 'ERROR';
                    } else {
                        $scope.error = 'ERROR';
                    }

                }).$promise.finally(function(){

                });
            }
        };


    });
