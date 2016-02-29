'use strict';

angular.module('ozayApp')
    .factory('OrganizationUser', function ($resource) {
        return $resource('api/organization-user/:method', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' }
            });
        });

