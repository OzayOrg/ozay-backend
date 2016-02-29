'use strict';

angular.module('ozayApp')
    .controller('NotificationRecordDetailController', function($scope, $state, $stateParams, NotificationTrack, Page, UserInformation) {
        $scope.button = true;
        $scope.contentTitle = 'Notification Archives';

        Page.get({
            state: $state.current.name,
            id:$stateParams.notificationId
        }).$promise.then(function(data) {
            $scope.notification = data.notification;
            $scope.notificationRecords = data.notificationRecords;
        });


    });
