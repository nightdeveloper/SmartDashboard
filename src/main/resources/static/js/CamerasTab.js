CamerasTab = function () {

    window.refreshCamera = function(id, url) {
        $("#" + id).attr('src', url + "&" + new Date().getMilliseconds());
    }

    this.init = function () {

        console.log("cameras tabs init...");

        var view = $("#cameras_views");

        if (cameras == null) {
            view.append("no cameras available");
            return;
        }

        function getView(camera, index) {
            var viewItem = $("<div class=\"w-50 camera-view\">" +
                "Camera <b>" + camera.name + "</b> " +
                "<button type=\"button\" class=\"btn btn-primary\" style='margin-left:50px'" +
                "  onclick='window.refreshCamera(\"camera" + index + "\", \"image?index=" + index + "\")' >Refresh</button> " +
                "<img id='camera" + index + "' src='image?index=" + index + "'/>" +
                "</div>");

            console.log("camera name = " + camera.name +", index = " + index);

            return viewItem;
        }

        for (var key in cameras) {
            if (cameras.hasOwnProperty(key)) {
                view.append(getView(cameras[key], key));
            }
        }
    }
};