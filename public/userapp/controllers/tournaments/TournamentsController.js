mainApp.controller('TournamentsController', ['$scope',  '$http', function ($scope, $http) {
    $http.get('api/tournaments', {}).
        success(function(data, status, headers, config) {
            $scope.tournaments = data;
            $('.collapsible').collapsible({
                accordion : false
            });
        }).error(function(data, status, headers, config, statusText) {
            notification("Error.", 4000, false)
        });

}]);