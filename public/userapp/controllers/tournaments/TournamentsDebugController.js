/**
 * Created by szymek on 08.03.15.
 */

mainApp.controller('TournamentsDebugController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {

        $http.get('api/tournaments/'+$stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.tournamentName = data.properties.description.name;
            }).error(function(data, status, headers, config, statusText) {
                notification("Sorry. An error occurred while loading tournament info.", 4000, false);
            });

        $scope.show = function(type) {
            $http.get('/api/tournaments/' + $stateParams.id + "/"+type).
                success(function(data, status, headers, config) {
                    $scope.message = JSON.stringify(data, null, 4);
                }).error(function(data, status, headers, config, statusText) {
                    $scope.message = "";
                    alert(status + " " + data)
                });
        };

        $scope.showcase = function(type) {
            $location.url("tournaments/" + $stateParams.id + "/"+type+ "Showcase");
        };

        $scope.generate = function(type){
            $http.post('/api/tournaments/'+$stateParams.id+'/'+type, {"tree":true}).
                success(function(data, status, headers, config) {
                    alert(status + " " + data)
                }).
                error(function(data, status, headers, config) {
                    window.alert(status + " " + data)
                });
        };

        $scope.remove = function(type){
            $http({
                url: '/api/tournaments/' + $stateParams.id + "/"+type,
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