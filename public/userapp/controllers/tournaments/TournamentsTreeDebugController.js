/**
 * Created by szymek on 08.03.15.
 */

mainApp.controller('TournamentsTreeDebugController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {

        $scope.show = function() {
            $http.get('/api/tournaments/' + $stateParams.id + "/tree").
                success(function(data, status, headers, config) {
                    $scope.message = data;
                }).error(function(data, status, headers, config, statusText) {
                    $scope.message = "";
                    alert(status + " " + data)
                });
        };

        $scope.showcase = function() {
            $location.url("tournaments/" + $stateParams.id + "/treeShowcase");
        };

        $scope.generate = function(){
            $http.post('/api/tournaments/'+$stateParams.id+'/tree', {"tree":true}).
                success(function(data, status, headers, config) {
                    alert(status + " " + data)
                }).
                error(function(data, status, headers, config) {
                    window.alert(status + " " + data)
                });
        };
        $scope.remove = function(){
            $http({
                url: '/api/tournaments/' + $stateParams.id + "/tree",
                dataType: 'json',
                method: 'DELETE',
                data: {
                    "tree":true
                },
                headers: {
                    "Content-Type": "application/json"
                }
            }).
                success(function(data, status, headers, config) {
                    alert(status + " " + data)

                }).
                error(function(data, status, headers, config, statusText) {
                    window.alert(status + " " + data)
                });

        };
}]);