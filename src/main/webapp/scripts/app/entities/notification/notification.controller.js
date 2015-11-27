'use strict';

angular.module('ozayApp')
.controller('NotificationController', function ($http, $scope, $filter, $rootScope, $cookieStore, Notification, Member, Role) {
	// initial settings
	$scope.button = true;
	$scope.showSuccessAlert = false;
	$scope.role = [];
	$scope.memberList = [];
	$scope.returnedMemberList = [];
	$scope.getRoles = function(){
		Role.query({building:$rootScope.selectedBuilding}).$promise.then(function(roles) {
			$scope.roleList = roles;
			angular.forEach(roles, function(role, key) {
				$scope.memberList.push({id:role.id, list:[]});
			});

			$scope.getAll();
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


	// Get people in the building
	$scope.getAll = function () {
		Member.query({building:$rootScope.selectedBuilding}, function(result) {
//			result = $filter('orderBy')(result, 'unit');
			$scope.individualList = [];
			$scope.returnedMemberList = result;
			angular.forEach(result, function(value, key) {
				angular.forEach(value.roles, function(role, key) {
					angular.forEach($scope.memberList, function(memberRole, key) {
						if(memberRole.id == role.id){
							memberRole.list.push(value);
						}
					});
				});
			});
			angular.forEach(result, function(value, key) {
				$scope.individualList.push({id: value.id, label: value.unit + " " + value.firstName + " " + value.lastName});
			});
		});
	};

	$scope.groupChanged = function(model, list){

		if(model == true){
			angular.forEach(list, function(value, key) {
				var hasUser = false;
				angular.forEach($scope.selectedUsers, function(user, userKey) {
					if(value.id == user.id){
						hasUser = true;
					}
				});

				if(hasUser == false){
					$scope.selectedUsers.push({id:value.id});
				}
			});
		}
		else {
			angular.forEach(list, function(value, key) {
				angular.forEach($scope.selectedUsers, function(user, userKey) {
					if(value.id == user.id){
						$scope.selectedUsers.splice(userKey, 1);
					}
				});
			});
		}
	}

	$scope.deselectModel = function(id){
		var member = null;
		angular.forEach($scope.returnedMemberList, function(value, key) {
			if(value.id == id){
				member = value;
			}
		});

		angular.forEach(member.roles, function(value, key) {
			$scope.role[value.id] = false;
		});
	}

	$scope.checkIfAllGroupItemSelected = function(id){
		var list = null
		angular.forEach($scope.memberList, function(value, key) {
			if(value.id == id){
				list = value.list;
			}
		});
		var existAll = false;
		if(list != null){
			existAll = true;
			angular.forEach(list, function(value, key){
				var hasMember = false;

				angular.forEach($scope.selectedUsers, function(user, key){
					if(user.id == value.id){
						hasMember = true;
					}
				});
				if(hasMember == false){
					existAll = false;
				}
			});
		}
		return existAll;
	}

	$scope.checkWhichGroup = function(id){
		var member = null;
		angular.forEach($scope.returnedMemberList, function(value, key) {
			if(value.id == id){
				member = value;
			}
		});

		angular.forEach(member.roles, function(value, key) {
			$scope.role[value.id] = $scope.checkIfAllGroupItemSelected(value.id);
		});
	}

	$scope.checkSubCategories = function(model, id){
		angular.forEach($scope.roleList, function(value, key) {
			if(value.id != id && value.belongTo == id){
				$scope.role[value.id] = model;
			}
		});
	}

	$scope.memberRoleClicked = function(id, model, model1){
		$scope.checkSubCategories(model, id);
		var list = null;
		angular.forEach($scope.memberList, function(value, key) {
			if(value.id == id){
				list = value.list;
			}
		});
		$scope.groupChanged(model, list);
	}

	$scope.onItemSelect = function(item){
		$scope.checkWhichGroup(item.id);
	}

	$scope.onItemDeselect = function(item){
		$scope.deselectModel(item.id);

	}
	$scope.onSelectAll = function(){
		angular.forEach($scope.roleList, function(value, key) {
			$scope.role[value.id] = true;
		});
	}
	$scope.onDeselectAll = function(){
		angular.forEach($scope.roleList, function(value, key) {
			$scope.role[value.id] = false;
		});
	}


	$scope.multiSelectSettings = {
			enableSearch: true,
			scrollableHeight: '350px',
			scrollable: true,
//			groupByTextProvider: function(groupValue) { if (groupValue === '1') { return 'Management'; }else if (groupValue === '2') { return 'Staff'; } else if (groupValue === '3') { return 'Board'; } else { return 'Resident'; } }

	};

	$scope.eventSettings ={
			onItemSelect: function(item){$scope.onItemSelect(item);},
			onItemDeselect: function(item){$scope.onItemDeselect(item);},
			onSelectAll:function(){$scope.onSelectAll();},
			onDeselectAll:function(){$scope.onDeselectAll();}
	}

	$scope.startProcess = function () {
		var result = $scope.selectedUsers.length;

		if(result == 0){
			alert("Pick at least one member");
			return false;
		}

		$scope.showSuccessAlert = false;
		$scope.showErrorAlert = false;
		var message = "Do you want to send to " + result + " recipients";
		if(confirm(message)){
			$scope.notification.buildingId = $rootScope.selectedBuilding;
			$scope.notification.memberIds = [];
			for(var i = 0; i<$scope.selectedUsers.length;i++){
				$scope.notification.memberIds.push($scope.selectedUsers[i].id);
			}
			Notification.save({building:$rootScope.selectedBuilding},$scope.notification,
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




	// End

	// Create Notification
	$scope.create = function () {
		$scope.button = false;
		$scope.startProcess();
	};

	$scope.notifications = [];

//	$scope.loadAll = function() {

//	Notification.get({buildingId:$rootScope.selectedBuilding}, function(result) {
//	$scope.notifications = result;
//	});
//	};
//	$rootScope.$watch('selectedBuilding', function() {
//	if($rootScope.selectedBuilding !== undefined){
//	$scope.loadAll();
//	}
//	});



	$scope.clear = function () {
		$scope.notification = {buildingId: null, notice: null, issueDate: null, createdBy: null, createdDate: null, id: null};
	};

	// Create notification and set today's date and set minDate(Notification cannot be scheduled for the past)
	$scope.notification = {};
	$scope.notification.issueDate = new Date();
	$scope.minDate = new Date();
	$scope.selectedUsers = [];


	$scope.selected = undefined;

	$scope.onSelect = function(item){
		$scope.notification.notice = item.notice;

	}
	$scope.getSubjects = function() {

		Notification.query({building:$rootScope.selectedBuilding, method:'latest', limit:"limit", limitNumber:10},function(result) {
			var items = [];
			for(var i=0;i<result.length;i++){
				items.push(result[i].subject);
			}
			$scope.subjects =  result;
		});
	};

	if($rootScope.selectedBuilding !== undefined){
		$scope.getSubjects();
	} else {
		$rootScope.$watch('selectedBuilding', function() {
			if($rootScope.selectedBuilding !== undefined){
				$scope.getSubjects();
			}
		});
	}

})

.controller('NotificationArchiveController', function ($scope, $filter, $rootScope, $cookieStore, Notification, Member, $sce) {

	$scope.trustAsHtml = function(html){
		return $sce.trustAsHtml(html);
	}
	$scope.predicate = '-createdDate';
	$scope.notifications = [];
	$scope.loadAll = function() {

		Notification.query({ building:$rootScope.selectedBuilding}, function(result) {
			$scope.notifications = result;
			//$scope.emailCount =notification.notificationRecordList.length;
		});
	};




	$rootScope.$watch('selectedBuilding', function() {
		if($rootScope.selectedBuilding !== undefined){
			$scope.loadAll();
		}
	});
})

.controller('NotificationTrackController', function ($scope, $filter, $rootScope, $cookieStore, Notification, Member, $sce) {

	$scope.trustAsHtml = function(html){
		return $sce.trustAsHtml(html);
	}
	$scope.predicate = '-createdDate';
	$scope.notifications = [];
	$scope.loadAll = function() {

		Notification.query({ building:$rootScope.selectedBuilding}, function(result) {
			$scope.notifications = result;
			//$scope.emailCount =notification.notificationRecordList.length;
		});
	};




	$rootScope.$watch('selectedBuilding', function() {
		if($rootScope.selectedBuilding !== undefined){
			$scope.loadAll();
		}
	});
})


.controller('NotificationArchiveViewController', function ($scope, $rootScope, $stateParams, Notification, $sce) {
	$scope.trustAsHtml = function(html){
		return $sce.trustAsHtml(html);
	}

	$scope.getNotification = function(){
		Notification.get({building:$rootScope.selectedBuilding, id:$stateParams.notificationId}).$promise.then(function(notification) {
			$scope.notification = notification;
			$scope.emailCount =notification.notificationRecordList.length;


		}, function(error){
			//$state.go('home.home');
		});
	}

	if($rootScope.selectedBuilding !== undefined){
		$scope.getNotification();
	} else {
		$rootScope.$watch('selectedBuilding', function() {
			if($rootScope.selectedBuilding !== undefined){
				$scope.getNotification();
			}
		});
	}
});
