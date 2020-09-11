Navigation = function () {

    var elements = {
        "sensors": "Sensors",
        "rates": "Currency Rates",
        "cameras": "Cameras",
        "discovery": "Discovery"
    };

    this.init = function () {

        console.log("navigation init...");

        window.updateNavigationLocation = function(locationValue) {
            console.log("navigation location = " + locationValue);

            for (var key in elements) {
                if (key === locationValue) {
                    $("#nav_" + key).addClass("active");
                    $("#content_" + key).removeClass("hidden");
                    $("#content_nav_" + key).removeClass("hidden");

                } else {
                    $("#nav_" + key).removeClass("active");
                    $("#content_" + key).addClass("hidden");
                    $("#content_nav_" + key).addClass("hidden");
                }
            }
        }

        function getNavElement(key) {
            var navItem = $("<li class=\"nav-item\">" +
                "<a class=\"nav-link\" id=\"nav_" + key + "\" href=\"#" + key + "\">" + elements[key] + "</a>" +
                "</li>");

            navItem.click(function () {
                window.updateNavigationLocation(key);
            });

            return navItem;
        }

        var nav = $("#tabs_nav");
        for (var key in elements) {
            nav.append(getNavElement(key));
        }

        var defaultElement = "sensors";

        var hash = window.location.hash;
        if (hash !== undefined && hash !== "") {
            hash = hash.replace("#", "");

            if ($("#nav_" + hash).length) {
                defaultElement = hash;
            }
        }

        console.log("navigation default = " + defaultElement);

        window.updateNavigationLocation(defaultElement);
    }
};