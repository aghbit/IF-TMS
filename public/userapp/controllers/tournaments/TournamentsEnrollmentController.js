/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TournamentsEnrollmentController', ['$scope', '$http', '$stateParams', '$location', 'ErrorMessageService',
    function ($scope, $http, $stateParams, $location, ErrorMessageService) {
        $http.get('api/tournaments/'+$stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.tournament = data;
                $('.collapsible').collapsible({
                    accordion : false
                });

            }).error(function(data, status, headers, config, statusText) {
                $scope.closeThisDialog();
                $location.url(status + "/" + data);
                notification("Sorry. An error occured loading tournament info.", 4000, false);
            });
        
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
                ErrorMessageService.content = data;
                $location.url(status+"/");
                notification("Team exists in db!", 4000, false)
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