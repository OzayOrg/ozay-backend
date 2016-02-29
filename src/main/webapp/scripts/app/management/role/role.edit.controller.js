'use strict';

angular.module('ozayApp')
    .controller('RoleEditController', function($scope, $state, $stateParams, Page, MessageService, Role, Auth, UserInformation) {
        if(UserInformation.getOrganizationId() != $stateParams.organizationId){
            Auth.authorize(true).then(function(){
                $state.reload();
            });
        }
        $scope.pageTitle = 'Role New';
        $scope.contentTitle = 'Role New';
        $scope.button = true;
        $scope.submitted = false;
        $scope.organizationId = $stateParams.organizationId;
        $scope.buildingId = $stateParams.buildingId;
        $scope.access = [];
        $scope.accessList = [];
        var roleId = $stateParams.roleId;

        Page.get({
            state: $state.current.name,
            id: roleId,
            building: $stateParams.buildingId
        }).$promise.then(function(data) {

            $scope.permissions = data.permissions;

            if (data.roles.length > 0) {
                $scope.showBelongTo = true;
                $scope.roles = data.roles;
            }

            $scope.organizationUserRoleDTOs = data.organizationUserRoleDTOs;

            for (var i = 0; i < data.permissions.length; i++) {
                $scope.accessList.push({
                    id: data.permissions[i].id,
                    label: data.permissions[i].label,
                });
            }
            if ($state.current.name == 'role-edit') {
                console.log(data.role);
                $scope.role = data.role;


                if (data.role.rolePermissions.length == 0) {
                    $scope.role.rolePermissions = [];
                } else {
                    for (var i = 0; i < $scope.role.rolePermissions.length; i++) {
                        var index = $scope.role.rolePermissions[i].permissionId;
                        $scope.access[index] = true;
                    }
                }
            }
        });

        if ($state.current.name == 'role-edit') {
            $scope.showDeleteBtn = true;
            $scope.contentTitle = 'Role Edit';
            $scope.pageTitle = 'Role Edit';
            var message = MessageService.getSuccessMessage();
            if(message !== undefined){
                $scope.successTextAlert = message;
            }
        } else {
            $scope.role = {};
            $scope.role.buildingId = $stateParams.buildingId;
            $scope.role.rolePermissions = [];
        }

        $scope.rolePermissionsClicked = function(value, modelValue) {
            if (modelValue == true) {
                $scope.role.rolePermissions.push({
                    permissionId: value
                });
            } else {
                for (var i = 0; i < $scope.role.rolePermissions.length; i++) {
                    if (value == $scope.role.rolePermissions[i].permissionId) {
                        $scope.role.rolePermissions.splice(i, 1);
                        break;
                    }
                }
            }
        }

        $scope.deleteClicked = function(){
            $scope.button = false;
            $scope.successTextAlert = null;
            $scope.errorTextAlert = null;

            if (confirm("Would you like to save?")) {
                Role.remove({id:roleId}, function(data) {
                    MessageService.setSuccessMessage('Successfully deleted');
                    $state.go('role', {organizationId:$stateParams.organizationId, buildingId:$stateParams.buildingId});
                }, function(error) {
                    if(error.data.message !== undefined){
                        $scope.errorTextAlert = error.data.message;
                    }else {
                        $scope.errorTextAlert = "Error! Please try later.";
                    }

                }).$promise.finally(function() {
                    $scope.button = true;
                });
            } else {
                $scope.button = true;
            }
        }

        $scope.submit = function() {
            $scope.button = false;
            $scope.successTextAlert = null;
            $scope.errorTextAlert = null;

            if (confirm("Would you like to save?")) {
                var form = {};
                form['role'] = $scope.role;
                form['organizationUserRoleDTOs'] = $scope.organizationUserRoleDTOs;
                console.log(form);
                if ($scope.role.id === undefined || $scope.role.id == 0) {
                    $scope.role.buildingId = $stateParams.buildingId;

                    Role.save(form, function(data) {
                        console.log(data);
                        MessageService.setSuccessMessage('Successfully created');
                        $state.go('role-edit', {organizationId:$stateParams.organizationId, buildingId:$stateParams.buildingId, roleId:data.id});
                    }, function(error) {
                        $scope.errorTextAlert = "Error! Please try later.";
                    }).$promise.finally(function() {
                        $scope.button = true;
                    });
                } else {
                    Role.update(form, function(data) {
                        $scope.successTextAlert = 'Successfully updated';
                    }, function(error) {
                        $scope.errorTextAlert = "Error! Please try later.";
                    }).$promise.finally(function() {
                        $scope.button = true;
                    });
                }
            } else {
                $scope.button = true;
            }
        };
    });
