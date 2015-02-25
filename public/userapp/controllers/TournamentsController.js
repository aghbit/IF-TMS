mainApp.controller('TournamentsController', ['$scope',  '$http', function ($scope, $http) {
    //important! it loads js for datePicker.
    angular.element(document).ready(function(){
        $('.datepicker').pickadate({
            closeOnSelect: true,
            format:"yyyy-mm-dd"
        });
        $('.timepicker').pickatime({
            format:'HH:i:00',
            min:[6,00],
            max:[23,00],
            closeOnSelect: true
        })
    })
    $scope.submit = function(){
        alert("submit!")
        var enrollDeadline = $('#enrollDeadline')[0].value;
        var enrollDeadlineTime = $('#enrollDeadlineTime')[0].value;
        var begin = $('#begin')[0].value;
        var beginTime = $('#beginTime')[0].value;
        var end = $('#end')[0].value;
        var endTime = $('#endTime')[0].value;
        var extraBegin = $('#extraBegin')[0].value;
        var extraBeginTime = $('#extraBeginTime')[0].value;
        var extraEnd = $('#extraEnd')[0].value;
        var extraEndTime = $('#extraEndTime')[0].value;
        $http.post('/api/tournaments', {
            "description":{"name":$scope.tournamentName, "place":$scope.tournamentPlace, "description":$scope.tournamentDescription},
            "term":{"enrollDeadline": enrollDeadline+" "+enrollDeadlineTime,
                    "begin": begin+" "+beginTime,
                    "end": end+" "+endTime,
                    "extraBegin": extraBegin+" "+extraBeginTime,
                    "extraEnd": extraEnd+" "+extraEndTime},
            "settings":{"numberOfPitches":$scope.numberOfPitches, "numberOfTeams":$scope.numberOfTeams,
                        "canEnroll": true, "level":$scope.level, "discipline":$scope.discipline}
        }).success(function(){
            alert("ok!")
        }).error(function(){
            alert("error!")
        })

    }

}]);