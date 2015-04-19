/**
 * Created by krzysiek.
 */

(window);;function notification(message, displayLength, state, className, completeCallback) {
    className = className || "";

    // Select and append notification
    var container = $('#notification-container')
    var newNotification = createNotification(message);

    if(!container.empty()) {
        container.pop();
    }
    container.append(newNotification);

    newNotification.css({"top" : parseFloat(newNotification.css("top"))+35+"px",
        "opacity": 0,
        "background-color": (state ? "transparent" : "red"),
        "text-shadow": "0px 0px 3px white"
    });
    newNotification.velocity({"top" : "0px",
            opacity: 1},
        {duration: 300,
            easing: 'easeOutCubic',
            queue: false});

    // Allows timer to be pause while being panned
    var timeLeft = displayLength;
    var counterInterval = setInterval (function(){
        if (newNotification.parent().length === 0)
            window.clearInterval(counterInterval);

        if (!newNotification.hasClass("panning")) {
            timeLeft -= 100;
        }

        if (timeLeft <= 0) {
            newNotification.velocity({"opacity": 0, marginTop: '-40px'},
                { duration: 375,
                    easing: 'easeOutExpo',
                    queue: false,
                    complete: function(){
                        if(typeof(completeCallback) === "function")
                            completeCallback();
                        $(this).remove();
                    }
                }
            );
            window.clearInterval(counterInterval);
        }
    }, 100);



    function createNotification(html) {
        var notification = $("<div class='notification'></div>")
            .addClass(className)
            .html(html);
        // Bind hammer
        notification.hammer({prevent_default:false
        }).bind('pan', function(e) {

            var deltaX = e.gesture.deltaX;
            var activationDistance = 80;

//                  change notification state
            if (!notification.hasClass("panning"))
                notification.addClass("panning");

            var opacityPercent = 1-Math.abs(deltaX / activationDistance);
            if (opacityPercent < 0)
                opacityPercent = 0;

            notification.velocity({left: deltaX, opacity: opacityPercent }, {duration: 50, queue: false, easing: 'easeOutQuad'});

        }).bind('panend', function(e) {
            var deltaX = e.gesture.deltaX;
            var activationDistance = 80;

            // If notification dragged past activation point
            if (Math.abs(deltaX) > activationDistance) {
                notification.velocity({marginTop: '-40px'},
                    { duration: 375,
                        easing: 'easeOutExpo',
                        queue: false,
                        complete: function(){
                            if(typeof(completeCallback) === "function")
                                completeCallback();
                            notification.remove()
                        }
                    })
            } else {
                notification.removeClass("panning");
                // Put notification back into original position
                notification.velocity({left: 0, opacity: 1},
                    { duration: 300,
                        easing: 'easeOutExpo',
                        queue: false
                    })
            }
        });
        return notification;
    }
};