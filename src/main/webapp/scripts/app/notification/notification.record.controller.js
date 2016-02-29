'use strict';

angular.module('ozayApp')
    .controller('NotificationRecordController', function($scope, $state, $stateParams, NotificationRecord, Page, UserInformation) {
        $scope.button = true;
        $scope.contentTitle = 'Notification Archive';

//        $scope.process = function(pageNumber){
//            Page.get({
//                state: $state.current.name,
//                page:pageNumber
//            }).$promise.then(function(data) {
//                $scope.totalItems = data.totalNumOfPages/2;
//                $scope.notifications = data.notificationRecords;
//            });
//        }

        // pagination

        $scope.setPage = function(pageNo) {
            $scope.currentPage = pageNo;
        };




        $scope.pageChanged = function() {
            $state.go('notification-record', {pageId:$scope.currentPage});
        };

        $scope.maxSize = 8;

        Page.get({
            state: $state.current.name,
            page:$stateParams.pageId
        }).$promise.then(function(data) {
            $scope.totalItems = data.totalNumOfPages/2;
            $scope.notifications = data.notificationRecords;
        });

    });
