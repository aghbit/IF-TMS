mainApp.controller('TournamentsMyTournamentsController', ['$scope',  '$http', function ($scope, $http) {
    $scope.testmessage = "You have to be logged in to see user details!"
    $http.get('api/tournaments', {}).
        success(function(data, status, headers, config) {
            $scope.tournaments = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {

        });

}]);