'use strict';

angular.module('ozayApp')
    .controller('OrganizationDetailController', function($scope, $state, $stateParams, Page, Auth, Principal, Organization, UserInformation) {
        if(UserInformation.getOrganizationId() != $stateParams.organizationId){
            Auth.authorize(true).then(function(){
                $state.reload();
            });
            return false;
        }
        if(!Principal.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_ORGANIZATION_SUBSCRIBER', 'BUILDING_GET', 'ORGANIZATION-USER_GET'])){
            if(Principal.hasAuthority('ROLE_GET')){
                $state.go('role', {organizationId:$stateParams.organizationId, buildingId:UserInformation.getBuilding().id});
                return false;
            }
        }

        $scope.pageTitle = 'Organization Detail';
        $scope.predicate1 = 'lastName';
        $scope.predicate2 = 'name';
        $scope.organizationId = $stateParams.organizationId;


        Page.get({
            state: $state.current.name,
            id: $stateParams.organizationId
        }).$promise.then(function(data) {
            $scope.buildings = data.buildings;
            $scope.users = data.organizationUserDTOs;
        });
    });
