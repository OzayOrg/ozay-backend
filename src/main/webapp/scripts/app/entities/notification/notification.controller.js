'use strict';

angular.module('ozayApp')
.controller('NotificationController', function ($scope, $filter, $rootScope, $cookieStore, Notification, Member, Role) {

    	$scope.getRoles = function(){
            Role.query({method:"building", buildingId:$rootScope.selectedBuilding}).$promise.then(function(roles) {

                            $scope.roleList = roles;
                        }, function(error){

                        });
    	}

    if($rootScope.selectedBuilding === undefined || $rootScope.selectedBuilding == 0){
            $rootScope.$watch('selectedBuilding', function(){
                if($rootScope.selectedBuilding !== undefined){
                    $scope.getRoles();
                }
            });
    	} else {
    		$scope.getRoles();
    	}
	// initial settings
	$scope.button = true;
	$scope.showSuccessAlert = false;
    $scope.role = [];
	$scope.memberList = [];

	// Get people in the building
	$scope.getAll = function (method, id) {

		Member.get({method:method, id: id}, function(result) {
            $scope.individualList = [];
            $scope.memberList = result;
		    angular.forEach(result, function(value, key) {
                $scope.individualList.push({id: value.id, label: value.unit + " " + value.firstName + " " + value.lastName, role: '1'});
            });



//			managementList = result[0].memberList;
//			staffList = result[1].memberList;
//			boardList = result[2].memberList;
//			residentList = result[3].memberList;
//
//			for(var i = 0; i<managementList.length; i++){
//				$scope.individualList.push({id: managementList[i].id, label: managementList[i].unit + " " + managementList[i].firstName + " " + managementList[i].lastName, role: '1'});
//				memberList.push({id: managementList[i].id, label: managementList[i].firstName + " " + managementList[i].lastName, role: 'management'});
//			}
//
//			for(var i = 0; i<staffList.length; i++){
//				$scope.individualList.push({id: staffList[i].id, label: staffList[i].unit + " " +  staffList[i].firstName + " " + staffList[i].lastName, role: '2'});
//				memberList.push({id: staffList[i].id, label: staffList[i].firstName + " " + staffList[i].lastName, role: 'staff'});
//			}
//
//			for(var i = 0; i<boardList.length; i++){
//				$scope.individualList.push({id: boardList[i].id, label: boardList[i].unit + " " + boardList[i].firstName + " " + boardList[i].lastName, role: '3'});
//				memberList.push({id: boardList[i].id, label: boardList[i].firstName + " " + boardList[i].lastName, role: 'board'});
//			}
//
//			for(var i = 0; i<residentList.length; i++){
//				$scope.individualList.push({id: residentList[i].id, label: residentList[i].unit + " " +  residentList[i].firstName + " " + residentList[i].lastName, role: '4'});
//				memberList.push({id: residentList[i].id, label: residentList[i].firstName + " " + residentList[i].lastName, role: 'resident'});
//			}
		});
	};

	$scope.groupChanged = function(model, name){

		var list = null;
		if(name == 'management'){
			list = managementList;
		} else if(name == 'staff'){
			list = staffList;
		} else if(name == 'board'){
			list = boardList;
		} else if(name == 'resident'){
			list = residentList;
		} else {
			return ;
		}
		if(model == true){
			for(var i = 0; i<list.length; i++){
				var hasData = false;
				for(var j = 0; j < $scope.selectedUsers.length;j++){
					if(list[i].id == $scope.selectedUsers[j].id){
						hasData = true;
						break;
					}
				}
				if(hasData === false){
					$scope.selectedUsers.push({id:list[i].id});
				}
			}
		} else {
			for(var i = 0; i<list.length; i++){
				for(var j = 0; j < $scope.selectedUsers.length;j++){
					if(list[i].id == $scope.selectedUsers[j].id){
						$scope.selectedUsers.splice(j, 1);
					}
				}
			}
		}
	}

	$scope.deselectModel = function(id){

		for(var i = 0; i< memberList.length; i++){
			if(memberList[i].id == id){
				if(memberList[i].role == 'management'){
					$scope.notification.management = false;
				}
				else if(memberList[i].role  == 'staff'){
					$scope.notification.staff = false;
				}
				else if(memberList[i].role  == 'board'){
					$scope.notification.board = false;
				}
				else if(memberList[i].role  == 'resident'){
					$scope.notification.resident = false;
				}
			}
		}
	}

	$scope.checkIfAllGroupItemSelected = function(list){
		var hasAll = true;
		for(var i = 0; i < list.length; i++){
			var hasUser = false;
			for(var j = 0; j < $scope.selectedUsers.length; j++){
				if(list[i].id == $scope.selectedUsers[j].id){
					hasUser = true;
					break;
				}
			}

			if(hasUser == false){
				hasAll = false;
				break;
			}
		}
		return hasAll;
	}

	$scope.checkWhichGroup = function(id){

         angular.forEach($scope.memberList, function(value, key) {
            angular.forEach(value, function(role, key) {
                
                                         });
                             });

	}

	$scope.memberRoleClicked = function(id, model, model1){
	console.log(model1);

	}


	$scope.onItemSelect = function(item){
		$scope.checkWhichGroup(item.id);
	}

	$scope.onItemDeselect = function(item){
		//$scope.deselectModel(item.id);

	}
	$scope.onSelectAll = function(){

	      angular.forEach($scope.roleList, function(value, key) {
                         $scope.role[value.id] = true;
                     });

//		$scope.notification.management = true;
//		$scope.notification.staff = true;
//		$scope.notification.board = true;
//		$scope.notification.resident = true;
	}
	$scope.onDeselectAll = function(){
	angular.forEach($scope.roleList, function(value, key) {
                             $scope.role[value.id] = false;
                         });
//		$scope.notification.management = false;
//		$scope.notification.staff = false;
//		$scope.notification.board = false;
//		$scope.notification.resident = false;
	}


	$scope.multiSelectSettings = {
			enableSearch: true,
			scrollableHeight: '400px',
			scrollable: true,
			groupByTextProvider: function(groupValue) { if (groupValue === '1') { return 'Management'; }else if (groupValue === '2') { return 'Staff'; } else if (groupValue === '3') { return 'Board'; } else { return 'Resident'; } } };

	$scope.eventSettings ={
			onItemSelect: function(item){$scope.onItemSelect(item);},
			onItemDeselect: function(item){$scope.onItemDeselect(item);},
			onSelectAll:function(){$scope.onSelectAll();},
			onDeselectAll:function(){$scope.onDeselectAll();}
	}

	$scope.startProcess = function (method, id) {
		var result = $scope.selectedUsers.length;

		if($scope.notification.management == false
		&& $scope.notification.staff == false
		&& $scope.notification.board == false
		&& $scope.notification.resident == false
		&& $scope.notification.individual == false){
		    result = memberList.length;
		}

		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		var message = "Do you want to send to " + result + " recipients";
		if(confirm(message)){
			$scope.notification.buildingId = $rootScope.selectedBuilding;
			$scope.notification.individuals = [];
			for(var i = 0; i<$scope.selectedUsers.length;i++){
				$scope.notification.individuals.push($scope.selectedUsers[i].id);
			}
			Notification.save($scope.notification,
					function (data) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = data.response;
				$scope.notification.notice = "";
			}, function (error){
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "Error! Please try later.";
				$scope.button = true;
			});
		}
		$scope.button = true;
	};




	// Get All members in this building
	var building = $rootScope.selectedBuilding;
	if(building === undefined){
		building = $cookieStore.get('selectedBuilding');
	}
	$scope.getAll('building', building);
	// End

	// Create Notification
	$scope.create = function () {
		$scope.button = false;
		$scope.startProcess('building_user_count', $rootScope.selectedBuilding);
	};





	$scope.clear = function () {
		$scope.notification = {buildingId: null, notice: null, issueDate: null, createdBy: null, createdDate: null, id: null};
	};

	// Create notification and set today's date and set minDate(Notification cannot be scheduled for the past)
	$scope.notification = {};
//	$scope.notification.management = false;
//	$scope.notification.staff = false;
//	$scope.notification.board = false;
//	$scope.notification.resident = false;
//	$scope.notification.individual = false;

	$scope.notification.issueDate = new Date();
	$scope.minDate = new Date();
	$scope.selectedUsers = [];


}).controller('NotificationArchiveController', function ($scope, $filter, $rootScope, $cookieStore, Notification, Member) {

	$scope.predicate = '-createdDate';
	$scope.notifications = [];
	$scope.loadAll = function() {
		var method= 'building';
		Notification.get({method:method, id:$rootScope.selectedBuilding}, function(result) {
			$scope.notifications = result;
		});
	};
	$rootScope.$watch('selectedBuilding', function() {
		if($rootScope.selectedBuilding !== undefined){
			$scope.loadAll();
		}
	});
});
