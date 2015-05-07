/**
 * Created by szymek on 08.03.15.
 */
mainApp.controller('TournamentsTeamsShowController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {
    $http.get('api/tournaments/' + $stateParams.id, {}).
        success(function(data, status, headers, config) {
            $scope.tournament = data;
        }).error(function(data, status, headers, config, statusText) {

        });
    $http.get('api/tournaments/' + $stateParams.id+"/teams", {}).
        success(function(data, status, headers, config) {
            $scope.teams = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {
        });

    $scope.showContactInfo = function(team){
        console.log(team)
        ngDialog.open({
            template: '/assets/userapp/partials/teams/contactInfo.html',
            className: 'ngdialog-theme-plain',
            data: team,
            closeByDocument: true
        })
    };
    $scope.addAnotherPlayer = function(teamID){
        $location.path('/teams/' + teamID + '/addPlayer');
    };

    $scope.addAnotherTeam = function(){
        $location.path('/tournaments/' + $scope.tournament._id + '/enrollment');
    };
}]);