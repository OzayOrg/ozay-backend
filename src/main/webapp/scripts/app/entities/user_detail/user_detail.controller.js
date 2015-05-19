'use strict';

angular.module('ozayApp')
.controller('UserDetailController', function ($scope, $filter, UserDetail, $cookieStore, $rootScope) {
	$scope.getAll = function (method, id) {
		UserDetail.get({method:method, id: id}, function(result) {
			$scope.managementList = result[0].userDetailList;
			$scope.staffList = result[1].userDetailList;
			$scope.boardList = result[2].userDetailList;
			$scope.residentList = result[3].userDetailList;
		});
	};
	var building = $rootScope.selectedBuilding;
	if(building === undefined){
		building = $cookieStore.get('selectedBuilding');
	}

	$scope.getAll('building', building);

	$scope.isResident = function(renter){
		if(renter === true){
			return true;
		} else {
			return false;
		}
	}

})
.controller('DirectoryDetailController', function ($scope, $cookieStore, $routeParams, $location, $state, $stateParams, UserDetail, $rootScope) {
	if($stateParams.method != 'edit' && $stateParams.method != 'new'){
		$location.path('/error').replace();
	}

	$scope.submitted = false;
	$scope.UserDetail = {};
	$scope.UserDetail.user = {};

	$scope.type = 'EDIT';
	if($stateParams.method == 'new'){
		$scope.type = 'CREATE';
		var building = $rootScope.selectedBuilding;

		if(building === undefined){
			building = $cookieStore.get('selectedBuilding');
		}
		$scope.UserDetail.buildingId = building;
		$scope.UserDetail.management = false;
		$scope.UserDetail.staff = false;
		$scope.UserDetail.board = false;
		$scope.UserDetail.resident = false;
	}




	$scope.create = function () {
		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		$scope.button = false;
        if($scope.type == 'EDIT'){
            UserDetail.update($scope.UserDetail,
            				function (data) {
            			$scope.showSuccessAlert = true;
            			$scope.successTextAlert = "Successfully Saved";
            		}, function (error){
            			$scope.showErrorAlert = true;
            			$scope.errorTextAlert = "Error! Please try later.";
            			$scope.button = true;
            		});
        } else {
            UserDetail.save($scope.UserDetail,
            				function (data) {
            			$scope.showSuccessAlert = true;
            			$scope.successTextAlert = "Successfully Saved";
            		}, function (error){
            			$scope.showErrorAlert = true;
            			$scope.errorTextAlert = "Error! Please try later.";
            			$scope.button = true;
            		});
        }

		$scope.button = true;
	};



	$scope.button = true;
	$scope.roleList = [{
		name: 'management',
		label:'Management'
	},{
		name: 'staff',
		label:'Staff'
	},{
		name: 'board',
		label:'Board'
	},{
		name: 'resident',
		label:'Resident'
	}];

	$scope.renterList = [
	                     {
	                    	 value:true,
	                    	 label : 'Yes'
	                     },{
	                    	 value:false,
	                    	 label : 'No'
	                     }];

	$scope.getUser = function(method, id, login){
		UserDetail.getUser({method:method, id: id, login:login}, function(result) {

			if(result.unit == null){
				result.unit = "";
			}
			$scope.UserDetail = result;
			$scope.model.radioBox = result.renter;
		}, function(){

		    $state.go("home.directory");

		});
	}
	if($stateParams.method == 'edit'){
		var selectedBuildingId = $cookieStore.get('selectedBuilding');
		$scope.getUser('building', selectedBuildingId, $stateParams.memberId);
	}


	$scope.cancel = function(){
		$location.path('/directory').replace();
	}
	$scope.model = {
			name: 'renter',
			radioBox:undefined
	};

	$scope.changeRadio = function(obj){
		$scope.model = obj;
	};

	$scope.roleValidation = false;

	$scope.roleValidationCheck = function(){

		if($scope.UserDetail.management == false && $scope.UserDetail.staff == false && $scope.UserDetail.board == false && $scope.UserDetail.resident == false){
			$scope.roleValidation = false;
			return false;
		} else{
			$scope.roleValidation = true;
			return true;
		}
	}

	$scope.$watch('UserDetail.management', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('UserDetail.staff', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('UserDetail.board', function() {
		$scope.roleValidationCheck();
	});
	$scope.$watch('UserDetail.resident', function() {
		$scope.roleValidationCheck();
	});


});
