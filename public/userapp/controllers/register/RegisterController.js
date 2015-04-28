/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', '$http', '$location', function($scope, $http, $location) {

    $scope.validatePasswordField = function(){
        if($scope.password == null || $scope.password.length<5 || $scope.password.length > 20){
            $scope.passwordClass = "invalid";
            return false;
        }else{
            $scope.passwordClass = "";
            if($scope.password2 == null || $scope.password != $scope.password2){
                $scope.password2Class = "invalid";
                return false;
            }else {
                $scope.password2Class = "";
                return true;
            }
        }

    };

    $scope.checkLoginAvailability = function(){
        if($scope.login != null && $scope.login.length>5 && $scope.login.length<20){
            $http.get('/api/users/login/'+$scope.login).
                success(function (data, status, headers, config) {
                    $scope.loginClass = "invalid";
                    return false;
                }).
                error(function (data, status, headers, config){
                    $scope.loginClass = "";
                    return true;
                })
        }else{
            $scope.loginClass = "invalid";
        }
    }

    $scope.validateNameField = function() {
        if($scope.name == null || $scope.name.length < 3 || $scope.name.length > 20){
            $scope.nameClass = "invalid";
            return false;
        }else {
            $scope.nameClass = "";
            return true;
        }
    }

    $scope.validateMailField = function() {
        //regex for email address RFC 5322
        var pattern= /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
        if(!pattern.test($scope.mail)){
            $scope.mailClass = "invalid";
            return false;
        }else {
            $scope.mailClass = "";
            return true;
        }

    }
    $scope.validatePhoneField = function() {
        //regex for email address RFC 5322
        var pattern= /^[0-9]+$/
        if(!pattern.test($scope.phone)){
            $scope.phoneClass = "invalid";
            return false;
        }else {
            $scope.phoneClass = "";
            return true;
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
}]);
