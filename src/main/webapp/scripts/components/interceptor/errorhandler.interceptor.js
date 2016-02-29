'use strict';

angular.module('ozayApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope, $location) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('ozayApp.httpError', response);
	            }
	            if (response.status == 403){
                   $location.path('/#')
                }
                return $q.reject(response);
            }
        };
    });
