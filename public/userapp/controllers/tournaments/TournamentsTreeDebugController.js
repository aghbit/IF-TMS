/**
 * Created by szymek on 08.03.15.
 */

mainApp.controller('TournamentsTreeDebugController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {

        $http.post('/api/tournaments/'+$stateParams.id+'/tree', {"tree":true}).
            success(function(data, status, headers, config) {
                $http.get('/api/tournaments/' + $stateParams.id + "/tree").
                    success(function(data, status, headers, config) {
                        $scope.message = data;
                    }).error(function(data, status, headers, config, statusText) {

                    });
            }).
            error(function(data, status, headers, config) {
                window.alert("error!")
            });

}]);