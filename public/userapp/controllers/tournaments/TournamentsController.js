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
        if($("#list-icon"+id).hasClass("rotate-clockwise") || $("#list-icon"+id).hasClass("reverse-rotate-clockwise")) {
            $("#list-icon"+id).toggleClass("reverse-rotate-clockwise");
        }
        $("#list-icon"+id).toggleClass("rotate-clockwise");
        if($scope.openedTournamentItem !== undefined && $scope.openedTournamentItem!== id){
            $("#list-icon"+$scope.openedTournamentItem).toggleClass("rotate-clockwise");
            $("#list-icon"+$scope.openedTournamentItem).toggleClass("reverse-rotate-clockwise");
        }
        if($scope.openedTournamentItem === id){
            $scope.openedTournamentItem = undefined;
        } else {
            $scope.openedTournamentItem = id;
        }
    }

}]);