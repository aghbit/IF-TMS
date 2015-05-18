/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TeamsAddPlayerController', ['$scope', '$http', '$stateParams', 'SessionService', '$location',
    function ($scope, $http, $stateParams, SessionService, $location) {

        $scope.loggedIn = SessionService.isLoggedIn;

        $http.get('api/teams/' + $stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.team = data;

            }).error(function(data, status, headers, config, statusText) {
                notification("Something went wrong!", 4000, false)
            });

        $scope.submit = function () {
            $http.post('/api/teams/'+$stateParams.id+"/players", {
                "name": $scope.name,
                "surname": $scope.surname
            }).
                success(function (data, status, headers, config) {
                    notification("Player " + $scope.name + " was added!", 4000, true)
                    $scope.name = ''
                    $scope.surname = ''

                }).
                error(function (data, status, headers, config) {
                    notification("Can't add, probably too many players in team!", 4000, false)
                });

            setTimeout( function() {
                    $http.get('api/teams/' + $stateParams.id, {}).
                        success(function (data, status, headers, config) {
                            $scope.team = data;

                        }).error(function (data, status, headers, config, statusText) {
                            notification("Something went wrong!", 4000, false)
                        })
                }
                ,250);
        };

        $scope.endAdding = function(){
            $location.path('tournaments/myTournaments');
        }
}]);