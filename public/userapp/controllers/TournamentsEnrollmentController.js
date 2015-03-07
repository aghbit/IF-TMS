/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TournamentsEnrollmentController', ['$scope', '$http', '$stateParams', function ($scope, $http, $stateParams) {
    $scope.submit = function () {
        $http.post('/api/tournaments/'+$stateParams.id+"/teams", {
            "teamName": $scope.teamName,
            "captainName": $scope.captainName,
            "captainSurname": $scope.captainSurname,
            "captainPhone": $scope.captainPhone,
            "captainMail": $scope.captainMail
        }).
            success(function (data, status, headers, config) {
                toast("Team "+$scope.teamName+" was created!")
            }).
            error(function (data, status, headers, config) {
                window.alert("Team exists in db!")
            });

    };
}]);