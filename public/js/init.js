(function($){
  $(function(){
    $('.button-collapse').sideNav();
    $('.dropdown-button').dropdown(
        {hover: false,
        belowOrigin:true});
    $('.slider').slider({full_width: true});
  }); // end of document ready
})(jQuery); // end of jQuery name space