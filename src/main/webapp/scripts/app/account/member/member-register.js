'use strict';

angular.module('ozayApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('member-register', {
                url: '/member-register?key',
                data: {
                    authorities: [],
                    pageTitle: 'Member Registration'
                },
                views: {
                    'other@': {
                        templateUrl: 'scripts/app/account/member/member-register.html',
                        controller: 'MemberRegisterController'
                    }
                },
                resolve: {

                }
            })
            .state('member-register-complete', {
                url: '/member-register-complete',
                data: {
                    authorities: [],
                    pageTitle: 'Member Register Registration Complete'
                },
                views: {
                    'other@': {
                        templateUrl: 'scripts/app/account/member/member-register-complete.html',
                        controller: 'MemberRegisterCompleteController'
                    }
                },
                resolve: {

                }
            });
    });
