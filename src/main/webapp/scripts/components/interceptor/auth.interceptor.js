'use strict';

angular.module('ozayApp')
    .factory('authInterceptor', function ($rootScope, $q, $location, $stateParams, localStorageService, $cookies, $injector) {
        return {
            // Add authorization token to headers
            request: function (config) {
                config.headers = config.headers || {};
                var token = localStorageService.get('token');

                if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                    config.headers.Authorization = 'Bearer ' + token.access_token;
                }
                var str = config.url;

                if(str.indexOf('.html') == -1){

                    var userInformation = $injector.get('UserInformation');
                    if(userInformation.getBuilding() !== undefined){
                        config.url+=config.url.indexOf('?') === -1 ? '?' : '&'
                        config.url += 'building=' + userInformation.getBuilding().id;
                    }
                    var state  = $injector.get('$state');

                    if(state.current.parent !== undefined && state.current.parent != 'manage'){
                    }
                    else {
                         if($stateParams.organizationId !== undefined){
                            config.url+=config.url.indexOf('?') === -1 ? '?' : '&'
                            config.url += 'organization=' + $stateParams.organizationId;
                         } else if(userInformation.getOrganizationId() !== undefined){
                             config.url+=config.url.indexOf('?') === -1 ? '?' : '&'
                             config.url += 'organization=' + userInformation.getOrganizationId();
                         }
                    }


                }

                return config;
            }
        };
    })
    .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, localStorageService) {
        return {
            responseError: function (response) {
                // token has expired
                if (response.status === 401 && (response.data.error == 'invalid_token' || response.data.error == 'Unauthorized')) {
                    localStorageService.remove('token');

                    var Principal = $injector.get('Principal');
                    var UserInformation = $injector.get('UserInformation');

                    if (Principal.isAuthenticated()) {
                        var Auth = $injector.get('Auth');
                        Auth.authorize(true);
                    }
                    UserInformation.clear();
                }
                return $q.reject(response);
            }
        };
    });
