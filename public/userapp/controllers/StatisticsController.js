mainApp.controller('StatisticsController', ['$scope','$http','SessionService', function ($scope,$http, SessionService) {
    $scope.testmessage = "test messagee"
        $http.get('api/users/'+SessionService.token.substr(0,24), {}).
        success(function(data, status, headers, config) {
                $scope.testmessage = data;

        }).error(function(data, status, headers, config, statusText) {

        });


}]);