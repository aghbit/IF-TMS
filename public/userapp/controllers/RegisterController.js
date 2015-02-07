/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', function($scope) {
    $('.modal-trigger').leanModal();
    $('#registermodal').openModal();
    $scope.testmessage = "Register page";

}]);