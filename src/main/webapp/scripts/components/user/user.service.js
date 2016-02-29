'use strict';

angular.module('ozayApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
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


angular.module('ozayApp')
    .service('UserInformation', function UserInformation($q, $cookies, Building, $stateParams, $state) {
        var building = undefined;
        var buildingList = [];
        var organizationId = undefined;

        return {
            clear:function(){
                building = undefined;
                buildingList = [];
                organizationId = undefined;
            },
            getBuilding:function(){
                return building;
            },
            getBuildingList : function(){
                return buildingList;
            },
            getOrganizationId:function(){
                return organizationId;
            },
            setBuilding:function(newBuildingId){
                for(var i = 0; i<buildingList.length;i++){
                    if(newBuildingId ==buildingList[i].id){
                        building = buildingList[i];
                    }
                }
            },
            setOrganizationId:function(id){
                organizationId = id;
            },
            process: function () {
                var deferred = $q.defer();
                // retrieve the identity data from the server, update the identity object, and then resolve.
                Building.query().$promise
                    .then(function (list) {
                        if(list.length == 0){
                            building = undefined;
                            buildingList = [];
                        }

                        buildingList = list;

                        var cookieBuildingId = $cookies.get('selectedBuilding');

                        for(var i = 0; i<buildingList.length;i++){
                            if(cookieBuildingId ==buildingList[i].id){
                                building = buildingList[i];
                            }
                        }

                        if(building === undefined){
                            building = buildingList[0];
                        }

                        if(building !== undefined){
                            organizationId = building.organizationId;
                            $cookies.put('selectedBuilding', building.id);
                        }
                        if($stateParams.organizationId !== undefined){
                            organizationId = $stateParams.organizationId;
                        }

                        if(Object.keys($stateParams).length == 0 && organizationId === undefined){ // Maybe $stateparmas are not initialized
                            var url = window.location.toString();
                            if(url.indexOf('/organization/') !== -1){
                                var pieces = url.split("/");
                                for(var i = 0; i < pieces.length;i++){
                                    if(pieces[i] == 'organization'){
                                        if(pieces[i+1]!==undefined && isNaN(pieces[i+1])){
                                            organizationId = pieces[i+1];
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        deferred.resolve(building);
                    })
                    .catch(function() {
                        building = undefined;
                        buildingList = [];
                        organizationId = null;
                        deferred.resolve(building);
                    });
                return deferred.promise;
            }
        };
    });
