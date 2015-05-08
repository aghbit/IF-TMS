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


    $scope.startEnrollment = function(id){
        $http.post('/api/tournaments/startEnrollment', {
            "_id":id
        }).success(function(){
            notification("Tournament's enrollment started!", 4000, true)
        }).error(function(){
            notification("Enrollment cannot be started!", 4000, false)
        })

    };

    $scope.stopEnrollment = function(id){
        $http.post('/api/tournaments/startEnrollment', {
            "_id":id
        }).success(function(){
            $scope.isEnrollmentStopped = true
            notification("Tournament's enrollment stopped!", 4000, true)
        }).error(function(){
            notification("Enrollment cannot be stopped!", 4000, false)
        })

    };

}]);