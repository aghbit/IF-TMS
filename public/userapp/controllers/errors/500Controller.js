/**
 * Created by szymek on 28.04.15.
 */
mainApp.controller('500Controller', ['$scope', '$stateParams', function ($scope, $stateParams) {
    $scope.message = "500 Internal Server Error: " + $stateParams.message;
}]);