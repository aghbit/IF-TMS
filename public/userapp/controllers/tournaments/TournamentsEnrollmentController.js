/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TournamentsEnrollmentController', ['$scope', '$http', '$stateParams', '$location', 'ErrorMessageService', '$sce',
    function ($scope, $http, $stateParams, $location, ErrorMessageService, $sce) {
        $http.get('api/tournaments/'+$stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.tournamentId = data._id;
                $scope.tournament = data.properties;
                $scope.participantType = data.participantType;
                $('.collapsible').collapsible({
                    accordion : false
                });

            }).error(function(data, status, headers, config, statusText) {
                $scope.closeThisDialog();
                $location.url(status + "/" + data);
                notification("Sorry. An error occurred while loading tournament info.", 4000, false);
            });
        
        $scope.submitTeam = function () {
            $http.post('/api/tournaments/'+$stateParams.id+"/teams", {
                "teamName": $scope.teamName,
                "captainName": $scope.captainName,
                "captainSurname": $scope.captainSurname,
                "captainPhone": $scope.captainPhone,
                "captainMail": $scope.captainMail
            }).
                success(function (data, status, headers, config) {
                    notification("Team " + $scope.teamName + " was created!", 4000, true)
                    $location.path("/participants/"+data.id+"/addPlayer")
                }).
                error(function (data, status, headers, config) {
                    ErrorMessageService.content = data;
                    $location.url(status+"/");
                    notification("Team exists in db!", 4000, false)
                });

        };

        $scope.submitPlayer = function () {
            console.log({
                "playerName": $scope.playerName,
                "playerSurname": $scope.playerSurname,
                "playerPhone": $scope.playerPhone,
                "playerMail": $scope.playerMail
            });
            $http.post('/api/tournaments/'+$stateParams.id+"/players", {
                "playerName": $scope.playerName,
                "playerSurname": $scope.playerSurname,
                "playerPhone": $scope.playerPhone,
                "playerMail": $scope.playerMail
            }).
                success(function (data, status, headers, config) {
                    notification("Player " + $scope.playerName + " was created!", 4000, true)
                    $location.path("/tournaments/"+$scope.tournamentId+"/participants")
                }).
                error(function (data, status, headers, config) {
                    ErrorMessageService.content = data;
                    $location.url(status+"/");
                    notification("Player exists in db!", 4000, false)
                });

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

        $scope.validatePattern = function(value, pattern) {
            var regex = new RegExp(pattern, 'g');
            if(!regex.test(value)){
                return "invalid";
            }else {
                return "";
            }
        };
}]);