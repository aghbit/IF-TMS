(function($){
    $(function(){
        var expand = 1;
        alert("u wladzy");
//  document.getElementById('panel').value = '1000';

        $('#panel').click(function() {
            alert("hehe");
            if (expand == 1) {
                $(this).animate({height: 800}, "slow");

                expand = 0;
            } else {
                $(this).animate({height: 130}, "slow");

                expand = 1;
            }
        });
        $('.button-collapse').sideNav();
        $('.dropdown-button').dropdown(
            {hover: false,
                belowOrigin:true});
        $('.slider').slider({full_width: true});
    }); // end of document ready
})(jQuery); // end of jQuery name space