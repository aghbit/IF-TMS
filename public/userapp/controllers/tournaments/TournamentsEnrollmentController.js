/**
 * Created by Szymek on 07.03.15.
 */
mainApp.controller('TournamentsEnrollmentController', ['$scope', '$http', '$stateParams', '$location',
    function ($scope, $http, $stateParams, $location) {

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
                notification("Team exists in db!", 4000, false)
            });

    };

        $scope.validateTeamNameField = function() {
            if($scope.teamName == null || $scope.teamName.length < 3 || $scope.teamName.length > 20){
                $scope.teamNameClass = "invalid";
            }else {
                $scope.teamNameClass = "";
            }
        };
        $scope.validateCaptainNameField = function() {
            if($scope.captainName == null || $scope.captainName.length < 3 || $scope.captainName.length > 20){
                $scope.captainNameClass = "invalid";
            }else {
                $scope.captainNameClass = "";
            }
        };
        $scope.validateCaptainSurnameField = function() {
            if($scope.captainSurname == null || $scope.captainSurname.length < 3 || $scope.captainSurname.length > 20){
                $scope.captainSurnameClass = "invalid";
            }else {
                $scope.captainSurnameClass = "";
            }
        };
        $scope.validateCaptainPhoneField = function() {
            //regex for email address RFC 5322
            var pattern= /^[0-9]{9}$/
            if(!pattern.test($scope.captainPhone)){
                $scope.captainPhoneClass = "invalid";
            }else {
                $scope.captainPhoneClass = "";
            }

        };
        $scope.validateCaptainMailField = function() {
            //regex for email address RFC 5322
            var pattern= /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
            if(!pattern.test($scope.captainMail)){
                $scope.captainMailClass = "invalid";
            }else {
                $scope.captainMailClass = "";
            }

        };
}]);