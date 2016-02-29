'use strict';

angular.module('ozayApp')
    .factory('Role', function ($resource) {
        return $resource('api/role/:id', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' },
                'remove': { method:'DELETE' }
            });
        });

