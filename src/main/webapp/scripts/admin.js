'use strict';

/* App Module */
var httpHeaders;

var ozayApp = angular.module('ozayApp', ['http-auth-interceptor', 'tmh.dynamicLocale',
    'ngResource', 'ngRoute', 'ngCookies', 'ozayAppUtils', 'pascalprecht.translate', 'truncate']);

ozayApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider) {
            $routeProvider
            .when('/register', {
                                templateUrl: 'views/register.html',
                                controller: 'RegisterController',

                            })
                            .when('/activate', {
                                templateUrl: 'views/activate.html',
                                controller: 'ActivationController',

                            })
                .when('/notification_create', {
                    templateUrl: 'views/notification_create.html',

                })
                .when('/notification_track', {
                    templateUrl: 'views/notification_track.html',

                })
                .when('/notification_archive', {
                    templateUrl: 'views/notification_archive.html',

                })
                .when('/directory', {
                    templateUrl: 'views/directory.html',
                })

                .otherwise({
                    templateUrl: 'views/dashboard.html',
                 //   controller: 'MainController',

                });

            // Initialize angular-translate
            $translateProvider.useStaticFilesLoader({
                prefix: 'i18n/',
                suffix: '.json'
            });

            $translateProvider.preferredLanguage('en');

            $translateProvider.useCookieStorage();

            tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js')
            tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
            httpHeaders = $httpProvider.defaults.headers;
        })
        .run(function($rootScope, $location, $http) {

        });
