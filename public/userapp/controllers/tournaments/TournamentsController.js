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
    $scope.openedTournamentItem = undefined;
    $scope.rotateArrow = function(id){
        $("#list-icon"+id).toggleClass("rotate-clockwise");
        if($scope.openedTournamentItem !== undefined && $scope.openedTournamentItem!== id){
            $("#list-icon"+$scope.openedTournamentItem).toggleClass("rotate-clockwise");
        }
        if($scope.openedTournamentItem === id){
            $scope.openedTournamentItem = undefined;
        }
        else{
            $scope.openedTournamentItem = id;
        }
    }

}]);