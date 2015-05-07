/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', '$http', '$location', 'ngDialog', function ($scope, $http, $location, ngDialog) {

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

    $scope.validateNameField = function() {
        if($scope.name == null || $scope.name.length < 3 || $scope.name.length > 20){
            $scope.nameClass = "invalid";
        }else {
            $scope.nameClass = "";
        }
    }

    $scope.validateMailField = function() {
        //regex for email address RFC 5322
        var pattern= /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
        if(!pattern.test($scope.mail)){
            $scope.mailClass = "invalid";
        }else {
            $scope.mailClass = "";
        }

    }
    $scope.validatePhoneField = function() {
        //regex for email address RFC 5322
        var pattern= /^[0-9]{9}$/
        if(!pattern.test($scope.phone)){
            $scope.phoneClass = "invalid";
        }else {
            $scope.phoneClass = "";
        }

    }

    $scope.checkForm = function() {
        return $scope.mailClass == "" &&
            $scope.nameClass == "" &&
            $scope.passwordClass == "" &&
            $scope.mailClass == "" &&
            $scope.phoneClass == "" &&
            $scope.loginClass == ""
    }

    $scope.submit = function(){
        if($scope.checkForm()) {
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
                    $location.url(status + "/" + data);
                    notification("Sorry. Error occurred.", 4000, false);
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
