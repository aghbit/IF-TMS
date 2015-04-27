/**
 * Created by szymek on 08.03.15.
 */
mainApp.controller('TournamentsTeamsShowController', ['$scope', '$location', '$http', '$stateParams', 'SessionService',
    function ($scope, $location, $http, $stateParams, SessionService) {
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

    $scope.addAnotherPlayer = function(teamID){
        $location.path('/teams/' + teamID + '/addPlayer');
    }

}]);