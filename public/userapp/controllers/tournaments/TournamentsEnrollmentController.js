/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TournamentsEnrollmentController', ['$scope', '$http', '$stateParams', '$location',
    function ($scope, $http, $stateParams, $location) {
    $scope.submit = function () {
        $http.post('/api/tournaments/'+$stateParams.id+"/teams", {
            "teamName": $scope.teamName,
            "captainName": $scope.captainName,
            "captainSurname": $scope.captainSurname,
            "captainPhone": $scope.captainPhone,
            "captainMail": $scope.captainMail
        }).
            success(function (data, status, headers, config) {
                notification("Team " + $scope.teamName + " was created!", 4000, true)
                $location.path("/teams/"+data.id+"/addPlayer")
            }).
            error(function (data, status, headers, config) {
                notification("Team exists in db!", 4000, false)
            });

    };
}]);