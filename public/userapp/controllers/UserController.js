mainApp.controller('UserController', ['$scope','$http','SessionService', function ($scope,$http, SessionService) {
    $scope.testmessage = "You have to be logged in to see user details!"
        $http.get('api/users/'+SessionService.token.substr(0,24), {}).
        success(function(data, status, headers, config) {
                $scope.testmessage = data;

        }).error(function(data, status, headers, config, statusText) {

        });


}]);