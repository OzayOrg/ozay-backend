/* globals $ */
'use strict';

angular.module('ozayApp')
    .directive('pageTitle', function() {
        return {
            restrict: 'E',
            templateUrl: 'scripts/components/page/page-title.html'
        };
    });

