'use strict';

angular.module('ozayApp')
    .factory('Notification', function ($resource) {
        return $resource('api/notification/:id', {}, {
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
