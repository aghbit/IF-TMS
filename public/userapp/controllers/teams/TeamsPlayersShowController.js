/**
 * Created by szymek on 08.03.15.
 */
mainApp.controller('PlayersShowController', ['$scope',  '$http', '$stateParams', function ($scope, $http, $stateParams) {
    $http.get('api/participants/' + $stateParams.id, {}).
        success(function(data, status, headers, config) {
            $scope.team = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {
            notification("Something went wrong!", 4000, false)
        });

}]);