mainApp.controller('TournamentsMyTournamentsController', ['$scope',  '$http', function ($scope, $http) {
    $scope.testmessage = "You have to be logged in to see user details!"
    $http.get('api/myTournaments', {}).
        success(function(data, status, headers, config) {
            $scope.tournaments = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {

        });
    $scope.openedTournamentItem = undefined;
    $scope.rotateArrow = function(id){
        $bit("#list-icon"+id).toggleClass("rotate-clockwise");
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