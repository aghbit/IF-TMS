mainApp.controller('TournamentsMyTournamentsController', ['$scope',  '$http','$state', function ($scope, $http,$state) {
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


    $scope.checkOwner = function(tid){
        var cookie = $.cookie('tms-token');
        var id = cookie.substr(0,24)
        for (i = 0; i < $scope.tournaments.length; i++) {
            if($scope.tournaments[i]._id==tid) {
                if($scope.tournaments[i].staff.admin==id) return true
            }
        }
        return false
    };

    $scope.nextEnrollmentState = function(id){
        $http.post('/api/tournaments/nextEnrollmentState', {
            "_id":id
        }).success(function(){
            $state.reload();
            notification("Tournament's enrollment's state changed!", 4000, true)
        }).error(function(){
            notification("Enrollment state cannot be changed", 4000, false)
        })

    };

}]);