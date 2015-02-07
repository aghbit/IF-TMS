mainApp.controller('StatisticsController', ['$scope','$http','SessionService', function ($scope,$http, SessionService) {
    $scope.testmessage = "test message"
        $http.get('/statistics', {params:{token:SessionService.token}}).
        success(function(data, status, headers, config) {
            $scope.testmessage = data;

        });


}]);