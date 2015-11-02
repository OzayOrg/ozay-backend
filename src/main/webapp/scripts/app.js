'use strict';

/* App Module */
var httpHeaders;

var ozayApp = angular.module('ozayApp', ['http-auth-interceptor', 'tmh.dynamicLocale', 'ngSanitize',
                                         'ngResource', 'ngRoute', 'ngCookies', 'ozayAppUtils', 'pascalprecht.translate', 'truncate', 'ui.router', 'angularjs-dropdown-multiselect', 'naturalSort','textAngular', 'ui.bootstrap', 'FBAngular']);

ozayApp
.config(function ($routeProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider, $stateProvider, $urlRouterProvider, USER_ROLES) {

	$urlRouterProvider.otherwise('/');
	$stateProvider
	.state('login', {
		url: "/login",
		templateUrl: "views/login.html",
		controller: 'LoginController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('organization-user-activate', {
		url: "/organization-user-activate?key=value",
		templateUrl: "views/organization_user_activate.html",
		controller: 'OrganizationUserActivateController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('activate', {
		url: "/activate?key",
		templateUrl: "views/activate.html",
		controller: 'ActivationController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('accept-invitation', {
		url: "/accept-invitation?key",
		templateUrl: "views/activate_invitation.html",
		controller: 'InvitationActivationController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('loginRedirect', {
		url: "/login?redirect",
		templateUrl: "views/login.html",
		controller: 'LoginController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('logout', {
		url: "/logout",
		templateUrl: "views/login.html",
		controller: 'LogoutController',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})
	.state('error', {
		url:'/error',
		templateUrl: 'views/error.html',
		access: {
			authorizedRoles: [USER_ROLES.all]
		}
	})

//	.state('change_password', {
//	url : '/password',
//	templateUrl: 'views/password.html',
//	controller: 'PasswordController',
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	})
	.state('register', {
		url: "/register",
		templateUrl: "views/login.html",
		templateUrl: 'views/register.html',
		controller: 'RegisterController',
		access: {
			authorizedRoles: [USER_ROLES.admin]
		}
	})
	.state('home', {
		url: "",
		templateUrl: 'views/main.html',
		controller: 'MainController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.home', {
		url: '/',
		templateUrl: "/views/blank.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.dashboard', {
		url: "/dashboard",
		templateUrl: "/views/dashboard.html",
		controller: 'DashboardController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.notification_create', {
		url: "/notification_create",
		templateUrl: "/views/notification_create.html",
		controller: 'NotificationController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification_create, USER_ROLES.subscriber]
		}
	})
	.state('home.notification_archive', {
		url: "/notification_archive",
		templateUrl: "/views/notification_archive.html",
		controller: 'NotificationArchiveController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification_archive, USER_ROLES.subscriber]
		}
	})
	.state('home.notification_archive_view', {
		url: "/notification_archive/view/:notificationId",
		templateUrl: "/views/notification_archive_view.html",
		controller: 'NotificationArchiveViewController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification_archive, USER_ROLES.subscriber]
		}
	})
	.state('home.collaborate_create', {
		url: "/collaborate_create",
		templateUrl: "/views/collaborate_create.html",
		controller: 'NotificationController',
		access: {
        			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_notification_create, USER_ROLES.subscriber]
        		}
	})

	.state('home.collaborate_track', {
		url: "/collaborate_track",
		templateUrl: "/views/collaborate_track.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
//	.state('home.buildings', {
//	url : '/management/organization/:organizationId/buildings',
//	templateUrl: 'views/building.html',
//	controller: 'BuildingManageController',
//	access: {
//	authorizedRoles: [USER_ROLES.user]
//	}
//	})
	.state('home.building_create', {
		url: "/management/organization/:organizationId/buildings/new",
		controller:'BuildingController',
		templateUrl: "/views/building_detail.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.building_edit', {

		url: "/management/organization/:organizationId/buildings/edit/:buildingId",
		controller:'BuildingController',
		templateUrl: "/views/building_detail.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.role', {
		url: "/management/organization/:organizationId/buildings/:buildingId/roles",
		controller:'RoleController',
		templateUrl: "/views/role.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.role_create', {

		url: "/management/organization/:organizationId/buildings/:buildingId/roles/new",
		controller:'RoleDetailController',
		templateUrl: "/views/role_detail.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.role_edit', {

		url: "/management/organization/:organizationId/buildings/:buildingId/roles/edit/:roleId",
		controller:'RoleDetailController',
		templateUrl: "/views/role_detail.html",
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})

	.state('home.directory', {
		url: "/directory",
		templateUrl: 'views/directory.html',
		controller: 'MemberController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_directory_list, USER_ROLES.subscriber]
		}
	})
	.state('home.directory_edit', {
		url: "/directory/edit/:memberId",
		templateUrl: 'views/directory_details.html',
		controller: 'MemberDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.access_directory_edit , USER_ROLES.subscriber]
		}
	})
	.state('home.directory_details_new', {
		url: "/directory/:method",
		templateUrl: 'views/directory_details.html',
		controller: 'MemberDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.subscriber, USER_ROLES.access_directory_edit , USER_ROLES.subscriber]
		}
	})
	.state('home.password', {
		url : '/password',
		templateUrl: 'views/password.html',
		controller: 'PasswordController',
		access: {
			authorizedRoles: [USER_ROLES.user]
		}
	})
	.state('home.management', {
		url : '/management',
		templateUrl: 'views/management.html',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})
	.state('home.organization', {
		url : '/management/organization',
		templateUrl: 'views/organization.html',
		controller: 'OrganizationController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})
	.state('home.organization_view', {
		url : '/management/organization/view/:organizationId',
		templateUrl: 'views/organization_view.html',
		controller: 'OrganizationViewController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})

	.state('home.organization_edit', {
		url : '/management/organization/edit/:organizationId',
		templateUrl: 'views/organization_detail.html',
		controller: 'OrganizationDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})
	.state('home.organization_create', {
		url : '/management/organization/new',
		templateUrl: 'views/organization_detail.html',
		controller: 'OrganizationDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})
	.state('home.organization_users', {
		url : '/management/organization/:organizationId/users/',
		templateUrl: 'views/organization_users.html',
		controller: 'OrganizationUserController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})

	.state('home.organization_users_edit', {
		url : '/management/organization/:organizationId/users/edit/:userId',
		templateUrl: 'views/organization_users_detail.html',
		controller: 'OrganizationUserDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})
	.state('home.organization_users_create', {
		url : '/management/organization/:organizationId/users/new',
		templateUrl: 'views/organization_users_detail.html',
		controller: 'OrganizationUserDetailController',
		access: {
			authorizedRoles: [USER_ROLES.admin,USER_ROLES.access_management, USER_ROLES.subscriber]
		}
	})


	.state('home.search', {
		url: "/search/:item",
		templateUrl: 'views/search.html',
		controller: 'SearchController',
		access: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.user]
		}
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
.factory('authInterceptor', function ($rootScope, $injector, $q, $location, localStorageService) {
	return {

		// Add authorization token to headers
		request: function (config) {
			config.headers = config.headers || {};
			var token = localStorageService.get('token');
			if (token && token.expires_at && token.expires_at > new Date().getTime()) {
				config.headers.Authorization = 'Bearer ' + token.access_token;
			}

			return config;
		},
		responseError: function(response) {
			if (response.status == 401){
				var Auth = $injector.get('Auth');
				var $state = $injector.get('$state');
				var to = $rootScope.toState;
				var params = $rootScope.toStateParams;
				Auth.logout();
				$rootScope.returnToState = to;
				$rootScope.returnToStateParams = params;
				$state.go('login');

			}
			return $q.reject(response);
		}
	};
})
.run(function($rootScope, $cookieStore, $location, $http, $window, AuthenticationSharedService, Session, USER_ROLES, $state, Building) {

	$rootScope.authenticated = false;
	$rootScope.$on('$stateChangeSuccess', function (event, next) {
		$rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
		$rootScope.userRoles = USER_ROLES;
		AuthenticationSharedService.valid(next.access.authorizedRoles);
		$window.scrollTo(0,0);
	});

	// Call when the the client is confirmed
	$rootScope.$on('event:auth-loginConfirmed', function(data) {
		$rootScope.authenticated = true;

		if ($location.path() === "/login") {
			var search = $location.search();
			if (search.redirect !== undefined) {
				$location.path(search.redirect).search('redirect', null).replace();
			} else {
				$location.path('/').replace();
			}
		}
	});

	// Call when the 401 response is returned by the server
	$rootScope.$on('event:auth-loginRequired', function(rejection) {
		Session.invalidate();
		$rootScope.authenticated = false;
		if ($location.path() !== "/register" &&
				$location.path() !== "/activate" && $location.path() !== "/login") {
			var redirect = $location.path();
//			$location.path('/login').search('redirect', redirect).replace();

			if(redirect.length > 1 && redirect.charAt(0) == '/'){
				redirect = redirect.slice(1);
			} else {
				redirect = "";
			}
			$state.transitionTo('loginRedirect', {redirect:redirect}, {'reload':true});
		}
	});

	// Call when the 403 response is returned by the server
	$rootScope.$on('event:auth-notAuthorized', function(rejection) {
		$rootScope.errorMessage = 'errors.403';
		$location.path('/error').replace();
	});

	// Call when the user logs out
	$rootScope.$on('event:auth-loginCancelled', function() {
		$location.path('');
	});

	$rootScope.getBuildings = function(){
		Building.query(function(result) {
			if(result.length > 0){
				var building = $rootScope.selectedBuilding;
				if(building === undefined){
					// Check if building in cookie can be accessible to user
					var tempBuilding = $cookieStore.get('selectedBuilding');
					if(tempBuilding !== undefined || tempBuilding == false){
						var accessible = false;
						for(var i = 0; i< result.length; i++){
							if(result[i].id == tempBuilding){
								accessible = true;
								break;
							}
						}
						if(accessible === true){
							building = tempBuilding;
						}
					}
				}
				if(building === undefined){
					$cookieStore.put('selectedBuilding', result[0].id);
					building = result[0].id;
				}
				$rootScope.buildingList = result;
				$rootScope.selectedBuilding = building;
			}
			$rootScope.getAccountInfo();
		});
	}
});
ozayApp.controller('MainCtrl', function($scope, Fullscreen) {
	$scope.goFullscreen = function () {

      // Fullscreen
      if (Fullscreen.isEnabled())
         Fullscreen.cancel();
      else
         Fullscreen.all();
   };
});

