'use strict';

angular.module('ozayApp')
    .controller('OrganizationEditController', function($scope, $state, $stateParams, Page, Auth, Organization, UserInformation) {

        $scope.pageTitle = 'Organization Top';
        $scope.contentTitle = 'Organization New';
        $scope.button = true;
        $scope.submitted = false;
        if ($state.current.name == 'organization-edit') {
            $scope.contentTitle = 'Organization Edit';
            Page.get({
                state: 'organization',
                id: $stateParams.organizationId
            }).$promise.then(function(data) {
                $scope.organization = data;
            });
        }

        $scope.submit = function() {
            $scope.button = false;

            if (confirm("Would you like to save?")) {
                if ($scope.organization.id === undefined || $scope.organization.id == 0) {
                    Organization.save($scope.organization, function(data) {
                        $scope.successTextAlert = 'Successfully created';
                    }, function(error) {
                        $scope.errorTextAlert = "Error! Please try later.";

                    }).finally(function() {
                        $scope.button = true;
                    });
                } else {
                    Organization.update($scope.organization, function(data) {
                        $scope.successTextAlert = 'Successfully updated';
                    }, function(error) {
                        $scope.errorTextAlert = "Error! Please try later.";
                    }).$promise.finally(function() {
                        $scope.button = true;
                    });
                }
            }
        };
    });
