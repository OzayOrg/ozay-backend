'use strict';

angular.module('ozayApp')
    .controller('NavbarController', function($timeout, $scope, $location, $state, Auth, Principal, $cookies, ENV, UserInformation, Building) {
        $scope.activeMenu = $state;
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $scope.buildingReady = false;

        // Navigation
        $scope.panelClicked = function(panel) {
            var result = angular.element('#dropdown-' + panel).hasClass('in');
            if(panel == 'home'){
                result = false;
            }
            if (result == false) {
                angular.forEach(angular.element(".parent-li"), function(value, key) {
                    if (value.attributes['id'].value != 'parent-' + panel) {
                        var item = value.attributes['id'].value;
                        var pieces = item.split('-');
                        var child = pieces[1];
                        if (angular.element('#dropdown-' + child).hasClass('in')) {
                            $timeout(function() {
                                angular.element(value).trigger('click');
                            })
                        }
                    }
                });
            }
        }

        $scope.menuClicked = function(menu){
            $scope.activeMenu = menu;
        }


        if($state.current.parent == 'manage'){
            $scope.activeMenu = 'management';
            if($state.current.name != 'organization-detail' && $state.current.name != 'role'){
                $scope.activeChildMenu = 'management';
            } else{
                $scope.activeChildMenu = $state.current.name;
            }
        }
        else {
            var currentState =  $state.current.name;
            if(currentState.indexOf('-') !== -1){
                var pieces = currentState.split('-');
                currentState = pieces[0];
            }
            $scope.activeMenu = currentState;
            $scope.activeChildMenu = $state.current.name;
        }




        $scope.logout = function() {
            //            UserInformation.clear();
            Auth.logout();
            $state.go('login');
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        if (UserInformation.getBuildingList().length > 0) {
            $scope.buildingList = UserInformation.getBuildingList();
            $scope.selectedBuilding = UserInformation.getBuilding().id;
            $scope.organizationMenuExtra = false;
            if (UserInformation.getBuilding().id !== undefined) {
                $scope.orgName = UserInformation.getBuilding().organizationName;
                $scope.buildingName = UserInformation.getBuilding().name;
                $scope.organizationMenuExtra = true;
            }
            $scope.organizationId = UserInformation.getBuilding().organizationId;
        }

        if (UserInformation.getBuilding() !== undefined) {
            $scope.buildingReady = true;
        }


        $scope.keywordChange = function(newKey) {
            $scope.mobileKeyword = newKey;
        }
        $scope.mobileKeywordChange = function(newKey) {
            $scope.keyword = newKey;
        }
        $scope.searchBtnClicked = function() {
            if ($scope.keyword != "") {
                $state.go("search", {
                    key: $scope.keyword
                });
            }
        }
        $scope.searchBtnMobileClicked = function() {
            if ($scope.mobileKeyword != "") {
                $state.go("search", {
                    key: $scope.mobileKeyword
                });
            }
        }

        $scope.changeBuilding = function() {
            UserInformation.setBuilding($scope.selectedBuilding);
            $cookies.put('selectedBuilding', $scope.selectedBuilding);

            Auth.authorize(true).then(function() {
                $state.reload();
            });
        }
    });
