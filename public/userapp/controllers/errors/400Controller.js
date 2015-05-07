/**
 * Created by szymek on 28.04.15.
 */
mainApp.controller('400Controller', ['$scope', '$stateParams', function ($scope, $stateParams) {
    $scope.message = $stateParams.message;
}]);