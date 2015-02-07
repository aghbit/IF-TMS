/**
 * Created by Piotr on 2015-02-04.
 */
mainApp.controller('LoginController', ['$scope','$http','$location','ngDialog','SessionService', function($scope,$http,$location, ngDialog, SessionService) {
    $scope.credentials = {
        login : "login",
        password : "haslo"
    };
    $scope.submit = function(){
        $http.post('/token/'+$scope.credentials.login+"/"+$scope.credentials.password, {}).
            success(function(data, status, headers, config) {
                SessionService.token = data
                $location.url('statistics');
            }).
            error(function(data, status, headers, config) {
                window.alert("wrong credentials")
            });
    };
}]);