/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TeamsAddPlayerController', ['$scope', '$http', '$stateParams', 'SessionService', '$location', 'ErrorMessageService',
    function ($scope, $http, $stateParams, SessionService, $location, ErrorMessageService) {

        $scope.loggedIn = SessionService.isLoggedIn;

        $http.get('api/participants/' + $stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.team = data;

            }).error(function(data, status, headers, config, statusText) {
                notification("Something went wrong!", 4000, false)
            });

        $scope.submit = function () {
            $http.post('/api/participants/'+$stateParams.id+"/players", {
                "name": $scope.name,
                "surname": $scope.surname
            }).
                success(function (data, status, headers, config) {
                    notification("Player " + $scope.name + " was added!", 4000, true)
                    $scope.name = ''
                    $scope.surname = ''

                }).
                error(function (data, status, headers, config) {
                    ErrorMessageService.content = data;
                    $location.url(status+"/");
                    notification("Can't add, probably too many players in team!", 4000, false)
                });

            setTimeout( function() {
                    $http.get('api/participants/' + $stateParams.id, {}).
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
        };

        $scope.validateLength = function(value, min, max) {
            if(value == null) {
                return "This field is required.";
            } else if(value.length < min) {
                return "At least "+min+" characters required.";
            } else if(value.length > max) {
                return "No more than "+max+" characters allowed.";
            } else {
                return "";
            }
        };

}]);