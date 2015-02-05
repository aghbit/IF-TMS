var mainApp = angular.module('mainApp', ['ui.router', 'angular-loading-bar']);
mainApp.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/");

    $stateProvider
        .state('mainPage', {
            url: "/",
            templateUrl: "/assets/userapp/partials/main.html"
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

