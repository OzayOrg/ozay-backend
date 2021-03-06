'use strict';

angular.module('ozayApp')
.controller('DashboardController', function ($rootScope, $scope, $cookieStore, Dashboard) {
    $scope.dashboard = [];

	$rootScope.$watch('selectedBuilding', function(){
		if($rootScope.selectedBuilding !== undefined){
			var buildingId = $rootScope.selectedBuilding;
			Dashboard.get({building:buildingId},function(data) {
                $scope.dashboard = data;
			});
		}
	});
});
