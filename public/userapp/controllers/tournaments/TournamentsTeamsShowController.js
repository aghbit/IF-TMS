/**
 * Created by szymek on 08.03.15.
 */
mainApp.controller('TournamentsTeamsShowController', ['$scope',  '$http', '$stateParams', 'SessionService',
    function ($scope, $http, $stateParams, SessionService) {
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

}]);