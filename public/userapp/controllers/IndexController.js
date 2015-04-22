/**
 * Created by wojciech on 13.12.14.
 */
mainApp.controller('IndexController', ['$scope', 'ngDialog', function($scope, ngDialog) {
    $('.slider').slider({full_width: true});
    $scope.testmessage = "Index page";
    $scope.registerPopUp = function(){
        ngDialog.open({
            template: '/assets/userapp/partials/register/register.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    };
    $('.parallax-window').parallax({imageSrc: 'http://www.volleyballadvisors.com/image-files/volleyball-positions-libero.jpg'});
}]);