mainApp.controller('StatisticsController', ['$scope','$http','SessionService', function ($scope,$http, SessionService) {
    $scope.testmessage = "test messagee"
    //does not work yet. Statistics are not implemented.
        $http.get('api/statistics', {}).
        success(function(data, status, headers, config) {
                $scope.testmessage = data;

        }).error(function(data, status, headers, config, statusText) {

        });


}]);