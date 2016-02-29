'use strict';

angular.module('ozayApp')
    .controller('OrganizationUserEditController', function ($scope, $state, $stateParams, Page, Auth, OrganizationUser, MessageService, UserInformation) {
        if(UserInformation.getOrganizationId() != $stateParams.organizationId){
            Auth.authorize(true).then(function(){
                $state.reload();
            });
        }
        $scope.pageTitle = 'Organization User New';
        $scope.contentTitle = 'Organization User New';
        $scope.button = true;
        $scope.organizationId = $stateParams.organizationId;
        $scope.access = [];
        $scope.accessList = [];
        var organizationUserId = $stateParams.organizationUserId;


        Page.get({state: $state.current.name, id:organizationUserId, building:$stateParams.buildingId}).$promise.then(function(data){
            $scope.permissions = data.permissions;
            for(var i = 0; i< data.permissions.length;i++){
                $scope.accessList.push({
                    id: data.permissions[i].id,
                    label:data.permissions[i].label,
                });
            }
            if($state.current.name == 'organization-user-edit'){
                $scope.organizationUser = data.organizationUserDTO;
                var message = MessageService.getSuccessMessage();
                if(message !== undefined){
                    $scope.successTextAlert = message;
                }

                if(data.organizationUserDTO.organizationUserPermissions.length == 0){
                    $scope.organizationUser.organizationUserPermissions = [];
                } else{
                    for(var i = 0; i< $scope.organizationUser.organizationUserPermissions.length;i++){
                        var index = $scope.organizationUser.organizationUserPermissions[i].permissionId;
                        $scope.access[index] = true;
                    }
                }
            }
        }, function(){
            $state.go('error');
        });

        if($state.current.name == 'organization-user-edit'){
            $scope.contentTitle = 'Organization User Edit';
            $scope.pageTitle = 'Organization User Edit';
        }
        else{
            $scope.organizationUser = {};
            $scope.organizationUser.organizationId = $stateParams.organizationId;
            $scope.organizationUser.organizationUserPermissions = [];
        }

        $scope.organizationUserPermissionsClicked = function(value, modelValue){
            if(modelValue == true){
                $scope.organizationUser.organizationUserPermissions.push({permissionId:value});
            } else {
                for(var i = 0; i< $scope.organizationUser.organizationUserPermissions.length; i++){
                    if(value == $scope.organizationUser.organizationUserPermissions[i].permissionId){
                        $scope.organizationUser.organizationUserPermissions.splice(i, 1);
                        break;
                    }
                }
            }
        }

        $scope.sendInvitation = function(){
            if(confirm("Would you like to send invitation email?")){
                OrganizationUser.save({method:'invite'}, $scope.organizationUser, function (data) {
                    $scope.successTextAlert = "Successfully sent";
                }, function (error){
                    if(error.data.message !== undefined){
                        $scope.errorTextAlert = error.data.message;
                    } else {
                        $scope.errorTextAlert = "Error! Please try later.";
                    }
                }).$promise.finally(function(){
                    $scope.button = true;
                });
            }
        }

        $scope.submit = function (callback) {
            $scope.button = false;
            $scope.successTextAlert = null;
            $scope.errorTextAlert = null;

            if(confirm("Would you like to save?")){

                if($scope.organizationUser.id === undefined || $scope.organizationUser.id == 0){
                    $scope.organizationUser.organizationId = $stateParams.organizationId;

                    OrganizationUser.save($scope.organizationUser, function (data) {
                        MessageService.setSuccessMessage('Successfully created');
                        $state.go('organization-user-edit', {organizationId:$stateParams.organizationId, organizationUserId:data.id});
                    }, function (error){
                        if(error.data.message !== undefined){
                            $scope.errorTextAlert = error.data.message;
                        } else {
                            $scope.errorTextAlert = "Error! Please try later.";
                        }
                    }).$promise.finally(function(){
                        $scope.button = true;
                    });

                } else{
                    OrganizationUser.update($scope.organizationUser, function (data) {
                        $scope.successTextAlert = 'Successfully updated';
                    }, function (error){
                        if(error.data.message !== undefined){
                            $scope.errorTextAlert = error.data.message;
                        } else {
                            $scope.errorTextAlert = "Error! Please try later.";
                        }

                    }).$promise.finally(function(){
                        $scope.button = true;
                    });
                }
            }
        };
    });

