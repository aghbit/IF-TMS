/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', '$http', '$location', 'ngDialog', 'ErrorMessageService',
    function ($scope, $http, $location, ngDialog, ErrorMessageService) {

    $scope.validatePasswordField = function(){
        if($scope.password == null || $scope.password.length<5 || $scope.password.length > 20){
            $scope.passwordClass = "invalid";
        }else{
            $scope.passwordClass = "";
            if($scope.password2 == null || $scope.password != $scope.password2){
                $scope.password2Class = "invalid";
            }else {
                $scope.password2Class = "";
            }
        }

    };

    $scope.checkLoginAvailability = function(){
        if($scope.login != null && $scope.login.length>=5 && $scope.login.length<=20){
            $http.get('/api/users/login/'+$scope.login).
                success(function (data, status, headers, config) {
                    $scope.loginClass = "invalid";
                }).
                error(function (data, status, headers, config){
                    if(status==404){
                        $scope.loginClass ="";
                    }else{
                        $scope.closeThisDialog();
                        $location.url(status + "/" + "Internal Server Error. ")
                        notification("Sorry. Error occurred.", 4000, false);
                    }
                })
        }else{
            $scope.loginClass = "invalid";
        }
    }

        $scope.validateLength = function(value, min, max) {
            if(value == null) {
                return "This field is required.";
            } else if(value.length < min) {
                return "At least "+min+" characters required.";
            } else if(value.length > max) {
                return "No more than "+max+" characters allowed.";
            } else {
                return "";
            }
        };

        $scope.validatePattern = function(value, pattern) {
            var regex = new RegExp(pattern, 'g');
            if(!regex.test(value)){
                return "invalid";
            }else {
                return "";
            }
        };

    $scope.checkForm = function() {
        return $scope.mailClass == "" &&
            ($scope.nameError == "" || $scope.nameError == null) &&
            $scope.passwordClass == "" &&
            $scope.phoneClass == "" &&
            $scope.loginClass == ""
    }

    $scope.submit = function(){
        if(true) {
            $http.post('/api/users', {
                "name": $scope.name,
                "login": $scope.login,
                "password": $scope.password,
                "phone": $scope.phone,
                "mail": $scope.mail
            }).
                success(function (data, status, headers, config) {
                    $scope.closeThisDialog();
                    $location.url("/login");
                    notification("You have been successfully registered. You may now log in.", 4000, true);
                }).
                error(function (data, status, headers, config) {
                    $scope.closeThisDialog();
                    ErrorMessageService.content = data;
                    $location.url(status+"/");
                    notification("Sorry. An error occurred.", 4000, false);
                });
        }else {
            notification("Sorry. You have typed wrong data.", 4000, false);
        }
    };

    $scope.loginPopUp = function () {
        $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/login/login.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
}]);
