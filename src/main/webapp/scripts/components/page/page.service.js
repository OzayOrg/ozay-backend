'use strict';

angular.module('ozayApp')
    .factory('Page', function ($resource) {
        return $resource('api/page/:state/:id', {}, {
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
            });
        });
