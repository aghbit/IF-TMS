var mainApp = angular.module('mainApp', ['ui.router', 'angular-loading-bar','ngDialog']);
mainApp.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/");


    $stateProvider
        .state('mainPage', {
            url: "/",
            templateUrl: "/assets/userapp/partials/main.html",
            controller: 'IndexController'
        })
        .state('statistics', {
            url: "/statistics",
            templateUrl: "/assets/userapp/partials/statistics/statistics.html",
            controller: 'StatisticsController'
        })
        .state('users', {
            url: "/users",
            templateUrl: "/assets/userapp/partials/users/user.html",
            controller: 'UserController'
        })
        .state('tournaments', {
            url: "/tournaments",
            templateUrl: "/assets/userapp/partials/tournaments/main.html",
            controller: 'TournamentsController'
        })
        .state('tournaments/create', {
            url: "/tournaments/create",
            templateUrl: "/assets/userapp/partials/tournaments/create.html",
            controller: 'TournamentsCreateController'
        })
        .state('tournaments/myTournaments', {
            url: "/tournaments/myTournaments",
            templateUrl: "/assets/userapp/partials/tournaments/myTournaments.html",
            controller: 'TournamentsMyTournamentsController'
        })
        .state('tournaments/id/enrollment', {
            url: "/tournaments/{id}/enrollment",
            templateUrl: "/assets/userapp/partials/tournaments/enrollment.html",
            controller: 'TournamentsEnrollmentController'
        })
        .state('teams/id/addPlayer', {
            url: "/teams/{id}/addPlayer",
            templateUrl: "/assets/userapp/partials/players/addPlayer.html",
            controller: 'TeamsAddPlayerController'
        })
        .state('teams/id', {
            url: "/teams/{id}",
            templateUrl: "/assets/userapp/partials/players/show.html",
            controller: 'TeamsShowController'
        })
        .state('login', {
            url: "/login",
            templateUrl: "/assets/userapp/partials/login/login.html",
            controller: 'LoginController'
        })

});

mainApp.factory('SessionService', function() {
    var usersCredentials = {
        token : ' ',
        isLoggedIn : false,
        login : function () {
            ngDialog.open({
                template: 'templateId',
                className: 'ngdialog-theme-plain',
                closeByDocument: false
            });
        }
    };
    return usersCredentials;
});

//token always in headers
mainApp.factory('sessionInjector', ['SessionService', function(SessionService) {
    var sessionInjector = {
        request: function(config) {
            config.headers['token'] = SessionService.token;

            return config;
        }
    };
    return sessionInjector;
}]);
mainApp.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('sessionInjector');
}]);

//always check if user is authorized
var httpInterceptor = function ($provide, $httpProvider) {
    $provide.factory('httpInterceptor',function ($q, $window, $location) {
        return {
            response: function (response) {
                return response || $q.when(response);
            },
            responseError: function (rejection) {
                if(rejection.status === 401) {
                    $location.url('login');
                }
                return $q.reject(rejection);
            }
        };
    });
    $httpProvider.interceptors.push('httpInterceptor');
};
mainApp.config(httpInterceptor);