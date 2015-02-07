/**
 * Created by wojciech on 13.12.14.
 */
mainApp.controller('IndexController', ['$scope', 'ngDialog', function($scope, ngDialog) {
    $('.slider').slider({full_width: true});
    $scope.testmessage = "Index page";
    $scope.registerPopUp = function(){
        ngDialog.open({
            template: 'templateId',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
}]);