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
        .state('tournaments', {
            url: "/tournaments",
            templateUrl: "/assets/userapp/partials/tournaments/main.html",
            controller: 'TournamentsController'
        })
        //NOT USED
        //.state('register', {
        //    url: "/register",
        //    templateUrl: "/assets/userapp/partials/register/register.html",
        //    controller: 'RegisterController'
        //})
        .state('login', {
            url: "/login",
            templateUrl: "/assets/userapp/partials/login/login.html",
            controller: 'LoginController'
        })

});

mainApp.factory('SessionService', function() {
    var usersCredentials = {
        token : '12',
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
            config.headers['session-token'] = SessionService.token;

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