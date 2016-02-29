'use strict';

angular.module('ozayApp')
    .controller('NotificationController', function($scope, $state, Notification, MenuSearchState, Page, UserInformation) {
        $scope.button = true;
        $scope.role = [];
        $scope.memberList = [];
        $scope.returnedMemberList = [];
        $scope.selectedUsers = [];
        $scope.resetButton = 'Reset';
        $scope.contentTitle = 'Notification Create';

        Page.get({
            state: $state.current.name
        }).$promise.then(function(data) {
            $scope.roleList = data.roles;
            $scope.subjects = data.notifications;

            angular.forEach(data.roles, function(role, key) {
                $scope.memberList.push({
                    id: role.id,
                    list: []
                });
            });
            $scope.individualList = [];
            $scope.returnedMemberList = data.members;
            angular.forEach(data.members, function(value, key) {
                angular.forEach(value.roles, function(role, key) {
                    angular.forEach($scope.memberList, function(memberRole, key) {
                        if (memberRole.id == role.id) {
                            memberRole.list.push(value);
                        }
                    });
                });
            });
            angular.forEach(data.members, function(value, key) {
                if (value.unit == null || value.unit == 0) {
                    value.unit = "N/A";
                }
                $scope.individualList.push({
                    id: value.id,
                    label: value.unit + " " + value.firstName + " " + value.lastName
                });
            });
        });

        $scope.groupChanged = function(model, list) {
            if (model == true) {
                angular.forEach(list, function(value, key) {
                    var hasUser = false;
                    angular.forEach($scope.selectedUsers, function(user, userKey) {
                        if (value.id == user.id) {
                            hasUser = true;
                        }
                    });

                    if (hasUser == false) {
                        $scope.selectedUsers.push({
                            id: value.id
                        });
                    }
                });
            } else {
                angular.forEach(list, function(value, key) {
                    angular.forEach($scope.selectedUsers, function(user, userKey) {
                        if (value.id == user.id) {
                            $scope.selectedUsers.splice(userKey, 1);
                        }
                    });
                });
            }
        }

        $scope.deselectModel = function(id) {
            var member = null;
            angular.forEach($scope.returnedMemberList, function(value, key) {
                if (value.id == id) {
                    member = value;
                }
            });

            angular.forEach(member.roles, function(value, key) {
                $scope.role[value.id] = false;
            });
        }

        $scope.checkIfAllGroupItemSelected = function(id) {
            var list = null
            angular.forEach($scope.memberList, function(value, key) {
                if (value.id == id) {
                    list = value.list;
                }
            });
            var existAll = false;
            if (list != null) {
                existAll = true;
                angular.forEach(list, function(value, key) {
                    var hasMember = false;

                    angular.forEach($scope.selectedUsers, function(user, key) {
                        if (user.id == value.id) {
                            hasMember = true;
                        }
                    });
                    if (hasMember == false) {
                        existAll = false;
                    }
                });
            }
            return existAll;
        }

        $scope.checkWhichGroup = function(id, result) {
            var member = null;
            angular.forEach($scope.returnedMemberList, function(value, key) {
                if (value.id == id) {
                    member = value;
                }
            });
            if(result == false && member == null){
                $scope.role[id] = true;
                return;
            }

            if(member !== null){
                angular.forEach(member.roles, function(value, key) {
                    $scope.role[value.id] = $scope.checkIfAllGroupItemSelected(value.id, true);
                    if(value.belongTo > 0){
                        $scope.checkWhichGroup(value.belongTo, false);
                    }
                });
            }

        }

        $scope.checkSubCategories = function(model, id) {
            angular.forEach($scope.roleList, function(value, key) {
                if($scope.role[value.id] == model){
                    return;
                }
                if (value.id != id && value.belongTo == id) {
                    $scope.role[value.id] = model;
                    if(value.belongTo != 0){
                        $scope.memberRoleClicked(value.id, model, value);
                    }
                }
            });
        }

        $scope.memberRoleClicked = function(id, model, model1) {
            $scope.checkSubCategories(model, id);
            var list = null;
            angular.forEach($scope.memberList, function(value, key) {
                if (value.id == id) {
                    list = value.list;
                }
            });
            $scope.groupChanged(model, list);
        }

        $scope.onItemSelect = function(item) {
            $scope.checkWhichGroup(item.id);
        }

        $scope.onItemDeselect = function(item) {
            $scope.deselectModel(item.id);

        }
        $scope.onSelectAll = function() {
            angular.forEach($scope.roleList, function(value, key) {
                $scope.role[value.id] = true;
            });
        }
        $scope.onDeselectAll = function() {
            angular.forEach($scope.roleList, function(value, key) {
                $scope.role[value.id] = false;
            });
        }

        $scope.multiSelectSettings = {
            enableSearch: true,
            scrollableHeight: '350px',
            scrollable: true,
            //			groupByTextProvider: function(groupValue) { if (groupValue === '1') { return 'Management'; }else if (groupValue === '2') { return 'Staff'; } else if (groupValue === '3') { return 'Board'; } else { return 'Resident'; } }

        };

        $scope.eventSettings = {
            onItemSelect: function(item) {
                $scope.onItemSelect(item);
            },
            onItemDeselect: function(item) {
                $scope.onItemDeselect(item);
            },
            onSelectAll: function() {
                $scope.onSelectAll();
            },
            onDeselectAll: function() {
                $scope.onDeselectAll();
            }
        }

        $scope.onSelect = function(item) {
            $scope.notification.notice = item.notice;
        }
        $scope.reset = function() {
            $state.reload();
        }

        $scope.submit = function() {
            $scope.button = false;
            $scope.successTextAlert = null;
            $scope.errorTextAlert = null;
            var numOfRecipients = $scope.selectedUsers.length;
            var errorMessage = false;

            if (numOfRecipients == 0) {
                errorMessage = "You need to choose at least one recipient";
            }

            if (errorMessage !== false) {
                $scope.errorTextAlert = errorMessage;
                $scope.button = true;
                return false;
            }


            if (confirm("Would you like to send to " + $scope.selectedUsers.length + " people?")) {
                $scope.notification.buildingId = UserInformation.getBuilding().id;
                var form = {};
                form['notification'] = $scope.notification;
                form['members'] = $scope.selectedUsers;
                var roles = []
                for (var key in $scope.role) {
                    roles.push({
                        roleId: key,
                        checked: $scope.role[key]
                    });
                }
                form['roles'] = roles;

                Notification.save(form, function(data) {
                    $scope.successTextAlert = "Successfully sent";
                    $scope.resetButton = 'Send New Notification';
                    $scope.success = true;
                    $scope.track = true;
                }, function(error) {
                    $scope.errorTextAlert = "Error! Please try later.";
                }).$promise.finally(function() {
                    $scope.button = true;
                });

                $scope.checkboxModel = {
                       value1 : true
                      };


                $scope.track = function(notification){
                            // call api

                            Notification.update(notification, function(data) {
                                              notification.track = !notification.track;
                                                $scope.track = true;
                                            }, function(error) {
                                                $scope.errorTextAlert = "Error! Please try later.";
                                            }).$promise.finally(function() {
                                                $scope.track = true;
                                            });
                }



            }
        }
    });
