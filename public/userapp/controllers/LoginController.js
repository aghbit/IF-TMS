/**
 * Created by Piotr on 2015-02-04.
 */
mainApp.controller('LoginController', ['$scope', function($scope) {
    $('.modal-trigger').leanModal();
    $('#loginmodal').openModal();
    $scope.testmessage = "Login page";

}]);