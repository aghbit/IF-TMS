/**
 * Created by szymek on 08.03.15.
 */
mainApp.config(['$compileProvider',
    function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
    }]);
mainApp.controller('TournamentsTeamsShowController', ['$scope', '$location', '$http', '$stateParams', 'SessionService',
    function ($scope, $location, $http, $stateParams, SessionService) {
        $scope.getTournament = function(){
            $http.get('api/tournaments/' + $stateParams.id, {}).
                success(function(data, status, headers, config) {
                    $scope.tournament = data;
                }).error(function(data, status, headers, config, statusText) {

                });
        };
        $scope.getTeams = function() {
            $http.get('api/tournaments/' + $stateParams.id + "/teams", {}).
                success(function (data, status, headers, config) {
                    $scope.teams = data;
                    $('.collapsible').collapsible({
                        accordion: false
                    });
                }).error(function (data, status, headers, config, statusText) {

                });
        };
        $scope.getTournament();
        $scope.getTeams();
        $scope.generateCSV = function(){
        $scope.toParse = $scope.getTournament();
        var document = "";
        for(var i=0;i<$scope.teams.length;i++){
            var obj = $scope.teams[i];
            var teamName = obj.name;
            document = document+ teamName+",";
            document = document + obj.captain.name+ ","+obj.captain.surname+",";
            document = document + obj.captain.phone+",";
            document = document + obj.captain.mail+",";
            for(var j=0;j<obj.players.length-1;j++){
                document +=obj.players[j].name+ ","+ obj.players[j].surname+",";
            }
            document +=obj.players[obj.players.length-1].name+ ","+ obj.players[obj.players.length-1].surname+"\n";
        }
        //To share file to a client
        var blob = new Blob([ document ], { type : 'text/plain' });
        $scope.url = (window.URL || window.webkitURL).createObjectURL( blob );
    };


    $scope.addAnotherPlayer = function(teamID){
        $location.path('/teams/' + teamID + '/addPlayer');
    };


}]);