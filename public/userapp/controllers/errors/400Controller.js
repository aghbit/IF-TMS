/**
 * Created by szymek on 28.04.15.
 */
mainApp.controller('400Controller', ['$scope', '$stateParams', 'ErrorMessageService',
    function ($scope, $stateParams, ErrorMessageService) {
        $scope.message = ErrorMessageService.content;
        ErrorMessageService.content = "";
}]);