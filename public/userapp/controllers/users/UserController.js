/*
Created by ludwik who rocks

 */

mainApp.controller('UserController', ['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService){
    $scope.message = "You have to be logged in to see user details!";
    $scope.nameClass = "";
    $scope.loginClass = "";
    $scope.phoneClass = "";
    $scope.mailClass = "";
    $rootScope.user_id = SessionService.token.substr(0, 24);
    $scope.refresh = function() {
        $http.get('api/users/' + $rootScope.user_id, {}).
            success(function (data, status, headers, config) {
                //rootScope.login is login field in "Edit account" and
                //scope.login is field from popUp window
                $scope.user = data;
                $rootScope.login = $scope.user.userProperties.login;
                $rootScope.loginSave = $scope.user.userProperties.login;
                $rootScope.name = $scope.user.userProperties.name;
                $rootScope.phone = $scope.user.userProperties.phone;
                $rootScope.mail = $scope.user.userProperties.mail;
            }).error(function (data, status, headers, config, statusText) {

            });
    };
    $scope.refresh();
    $scope.checkLoginAvailability = function(){
        if($scope.login == null ||( ($scope.login.length>=5 && $scope.login.length<=20)) ){
            $http.get('/api/users/login/'+$scope.login).
                success(function (data, status, headers, config) {
                    $http.get('/api/users/checklogin/'+$scope.login).
                        success(function (data, status, headers, config) {

                                $scope.loginClass = "";
                        }).
                        error(function (data, status, headers, config){
                            if(status==404){
                                $scope.loginClass ="User with this login already exists.";
                            }
                            else{
                                $scope.closeThisDialog();
                                $location.url(status + "/" + "Internal Server Error. ")
                                notification("Sorry. Error occurred.", 4000, false);
                            }
                        })

                }). error(function (data, status, headers, config){
                    $scope.loginClass = "";
                });

        }else{
            $scope.loginClass = "Login should be 3 - 20 letters long.";
        }
    };
    $scope.validateNameField= function(){
        if(($scope.name != null) &&( $scope.name.length < 3 || $scope.name.length > 20)){
            $scope.nameClass = "Name should be 3 - 20 letters long.";
        }else {
            $scope.nameClass = "";
        }
    };


    $scope.validateRegexField = function(regex) {
        //regex for email address RFC 5322
        var phone= /^[0-9]{9}$/
        var mail= /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
        var pattern = "";
        if(regex=="phone")
            pattern = phone
        else if(regex=="mail")
            pattern = mail
        if(!pattern.test($scope.phone)){
            if($scope.phone==null)
                $scope.phoneClass = "";
            else
                $scope.phoneClass = "There should be nine digit long number.";

        }else {
            $scope.phoneClass = "";
        }

    };



    $scope.submit = function(){
        if($scope.mailClass == "" &&
            $scope.nameClass == "" &&
            $scope.phoneClass == "" &&
            $scope.loginClass == "")
        {
            $http.put('/api/users/' + SessionService.token.substr(0, 24), {
                "name": $scope.name,
                "login": $scope.login,
                "phone": $scope.phone,
                "mail": $scope.mail
            }).
                success(function (data, status, headers, config) {

                    $scope.closeThisDialog();
                    SessionService.token = data;
                    $rootScope.user_id = SessionService.token.substr(0, 24);
                    $.cookie('tms-token', SessionService.token, { expires: 1 });
                    notification("You have successfully edited your account.", 4000, true);
                    $location.url("/users");
                    $scope.refresh()
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


    $scope.editPopUp = function () {
      //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/edit.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
    $scope.passwordChangePopUp = function () {
        //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/passwordChange.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
    $scope.deletePopUp = function () {
        //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/delete.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
    $scope.delete = function(){
        $http.post('/api/users/' + SessionService.token.substr(0, 24)+"/delete", {
            "name": $scope.name,
            "login": $scope.login,
            "phone": $scope.phone,
            "mail": $scope.mail
        }).success(function (data, status, headers, config) {
            SessionService.token = " ";
            SessionService.isLoggedIn = false;
            $.removeCookie('tms-token');
            notification("Your account have been successfully deleted.", 4000, true);
            $location.url("");
            $rootScope.$emit("LOGIN_EVENT", false);
            $scope.loggedIn = SessionService.isLoggedIn;
            $scope.closeThisDialog();
            history.back();
        }).
            error(function (data, status, headers, config) {
                $scope.closeThisDialog();
                $location.url(status + "/" + data);
                notification("Sorry. Error occurred.", 4000, false);
            });

    }


}]);
