var mainApp = angular.module('mainApp', ['ui.router', 'angular-loading-bar','ngDialog', 'ngCookies', 'ngSanitize']);

mainApp.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/");


    $stateProvider
        // INDEX
        .state('mainPage', {
            url: "/",
            templateUrl: "/assets/userapp/partials/index.html",
            controller: 'IndexController'
        })


        //USERS
        .state('users', {
            url: "/users",
            templateUrl: "/assets/userapp/partials/users/user.html",
            controller: 'UserController'
        })


        // TOURNAMENTS
        .state('tournaments', {
            url: "/tournaments",
            templateUrl: "/assets/userapp/partials/tournaments/tournaments.html",
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
        .state('tournaments/id/teams', {
            url: "/tournaments/{id}/teams",
            templateUrl: "/assets/userapp/partials/tournaments/showTeams.html",
            controller: 'TournamentsTeamsShowController'
        })
        .state('tournaments/id/debug', {
            url: "/tournaments/{id}/debug",
            templateUrl: "/assets/userapp/partials/tournaments/debug.html",
            controller: 'TournamentsDebugController'
        }).state('tournaments/id/edit-table', {
            url: "/tournaments/{id}/edit-table",
            templateUrl: "/assets/userapp/partials/tournaments/editableTable.html",
            controller: 'TournamentTableController'
        })
        .state('tournaments/id/treeShowcase', {
            url: "/tournaments/{id}/treeShowcase",
            templateUrl: "/assets/userapp/partials/tournaments/treeShowcase.html",
            controller: 'TournamentsTreeShowcaseController'
        })
        .state('tournaments/id/tableShowcase', {
            url: "/tournaments/{id}/tableShowcase",
            templateUrl: "/assets/userapp/partials/tournaments/tableShowcase.html",
            controller: 'TournamentsTableShowcaseController'
        })


        // TEAMS
        .state('teams/id/addPlayer', {
            url: "/teams/{id}/addPlayer",
            templateUrl: "/assets/userapp/partials/teams/addPlayer.html",
            controller: 'TeamsAddPlayerController'
        })
        .state('teams/id', {
            url: "/teams/{id}",
            templateUrl: "/assets/userapp/partials/teams/showPlayers.html",
            controller: 'TeamsPlayersShowController'
        })


        // STATISTICS - UNUSED
        .state('statistics', {
            url: "/statistics",
            templateUrl: "/assets/userapp/partials/statistics/statistics.html",
            controller: 'StatisticsController'
        })

        //ERRORS
        .state('400', {
            url: "/400/{message}",
            templateUrl: "assets/userapp/partials/errors/400.html",
            controller: '400Controller'
        })
        .state('500', {
            url: "/500/{message}",
            templateUrl: "assets/userapp/partials/errors/500.html",
            controller: '500Controller'
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

mainApp.factory('ErrorMessageService', function() {
    var message = {
        content : ' '
    };
    return message;

});
