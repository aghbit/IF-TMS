/**
 * Created by szymek on 08.03.15.
 */
mainApp.controller('TeamsShowController', ['$scope',  '$http', '$stateParams', function ($scope, $http, $stateParams) {
    $http.get('api/teams/' + $stateParams.id, {}).
        success(function(data, status, headers, config) {
            $scope.team = data;
            $('.collapsible').collapsible({
                accordion : false
            });

        }).error(function(data, status, headers, config, statusText) {
            alert("Something went wrong!")
        });

}]);