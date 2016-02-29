'use strict';

angular.module('ozayApp')
    .factory('Member', function ($resource) {
        return $resource('api/member/:method/:id', {}, {
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

