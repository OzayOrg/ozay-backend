'use strict';

angular.module('ozayApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('member', {
                parent: 'site',
                url: '/member',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER', 'MEMBER_GET'],
                    pageTitle: 'Directory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/member/member.html',
                        controller: 'MemberController'
                    }
                },
                resolve: {

                }
            })
            .state('member-edit', {
                parent: 'site',
                url: '/member/edit/{memberId:int}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER', 'MEMBER_PUT', 'MEMBER_DELETE', 'MEMBER_GET'],
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/member/member.edit.html',
                        controller: 'MemberEditController'
                    }
                },
                resolve: {

                }
            })
            .state('member-new', {
                parent: 'site',
                url: '/member/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER', 'MEMBER_POST', 'MEMBER_GET'],
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/member/member.edit.html',
                        controller: 'MemberEditController'
                    }
                },
                resolve: {

                }
            });
    });
