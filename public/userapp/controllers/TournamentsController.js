mainApp.controller('TournamentsController', ['$scope', function ($scope) {
    //important! it loads js for datePicker.
    angular.element(document).ready(function(){
        $('.datepicker').pickadate();
        $('.timepicker').pickatime({
            min:[6,00],
            max:[23,00]
        })
    })
    $scope.testmessage = "Tournaments page";

}]);