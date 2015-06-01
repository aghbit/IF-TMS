(function($){
var expand = 1;
//  document.getElementById('panel').value = '1000';
    alert("Start");
$('#id').click(function() {
    alert("ALERt");
    if(expand==1) {
        $(this).animate({height: 800}, "slow");

        expand = 0;
    }else {
        $(this).animate({height: 130}, "slow");

        expand = 1;
    }
});})