
var mainApp = angular.module('mainApp', ['ui.router','angular-loading-bar']);
mainApp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/");

    $('.slider').slider({full_width: true});

    $stateProvider
        .state('mainPage',{
            url: "/",
            templateUrl: "/assets/userapp/partials/main.html",
            controller: 'StatisticsController'
        })
        .state('statistics', {
            url: "/statistics",
            templateUrl: "/assets/userapp/partials/statistics/main.html",
            controller: 'StatisticsController'
        })
        .state('tournaments', {
            url: "/tournaments",
            templateUrl: "/assets/userapp/partials/tournaments/main.html",
            controller: 'TournamentsController'
        })

});

