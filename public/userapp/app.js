
var mainApp = angular.module('mainApp', ['ui.router','angular-loading-bar']);
mainApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/");


    $stateProvider
        .state('mainPage',{
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
        .state('register', {
            url: "/register",
            templateUrl: "/assets/userapp/partials/register/register.html",
            controller: 'RegisterController'
        })

});

