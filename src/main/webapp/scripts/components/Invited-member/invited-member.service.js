'use strict';

angular.module('ozayApp')
    .factory('InvitedMember', function ($resource) {
        return $resource('api/invited-member/:method/:id', {}, {
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

