/**
 * Created by wojciech on 13.12.14.
 */
mainApp.controller('IndexController', ['$scope', function($scope) {
    $('.slider').slider({full_width: true});
    $scope.testmessage = "Index page";

}]);