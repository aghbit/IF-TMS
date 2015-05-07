/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TeamsAddPlayerController', ['$scope', '$http', '$stateParams', '$location',
    function ($scope, $http, $stateParams, $location) {
    $scope.submit = function () {
        $http.post('/api/teams/'+$stateParams.id+"/players", {
            "name": $scope.name,
            "surname": $scope.surname
        }).
            success(function (data, status, headers, config) {
                notification("Player " + $scope.name + " has been added!", 4000, true)
                $scope.name = ''
                $scope.surname = ''

            }).
            error(function (data, status, headers, config) {
                notification("Can't add, probably too many players in team!", 4000, false)
            });

    };
}]);