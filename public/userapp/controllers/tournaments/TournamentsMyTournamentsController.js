mainApp.controller('TournamentsMyTournamentsController', ['$scope',  '$http','$cookieStore', function ($scope, $http, $cookieStore) {
    $scope.testmessage = "You have to be logged in to see user details!"
    $http.get('api/myTournaments', {}).
        success(function(data, status, headers, config) {
            $scope.tournaments = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {

        });


    $scope.checkOwner = function(tid){
        var cookie = $cookieStore.get('tms-token');
        var id = cookie.substr(0,24)
        for (i = 0; i < $scope.tournaments.length; i++) {
            if($scope.tournaments[i]._id==tid) {
                if($scope.tournaments[i].staff.admin==id) return true
            }
        }
        return false
    };

    $scope.startStopEnrollment = function(id){
        $http.post('/api/tournaments/startStopEnrollment', {
            "_id":id
        }).success(function(){
            notification("Tournament's enrollment's state changed!", 4000, true)
        }).error(function(){
            notification("Enrollment state cannot be changed", 4000, false)
        })

    };

}]);