/**
 * Created by Piotr on 2015-02-04.
 */
mainApp.controller('LoginController', ['$scope','$http','$location','ngDialog','SessionService', function($scope,$http,$location, ngDialog, SessionService) {
    $scope.credentials = {
        login : "",
        password : ""
    };
    $scope.submit = function(){
        $http.post('/api/login', {"login" : $scope.credentials.login, "password": $scope.credentials.password}).
            success(function(data, status, headers, config) {
                SessionService.token = data;
                SessionService.isLoggedIn = true;
                $scope.$emit("LOGIN_EVENT", true);
                notification("You have been successfully logged in.", 4000, true);
                history.back()
            }).
            error(function(data, status, headers, config) {
                notification("Sorry. Wrong credentials.", 4000, false);
            });
    };
}]);