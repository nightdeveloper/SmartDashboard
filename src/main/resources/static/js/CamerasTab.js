CamerasTab = function () {

    this.init = function () {

        console.log("cameras tabs init...");

        var view = $("#cameras_views");

        if (cameras == null) {
            view.append("no cameras available");
            return;
        }

        function getView(camera, index) {
            var viewItem = $("<div class=\"col camera-view\">" +
                "Camera: " + camera.name + " " +
                "<img src='image?index=" + index + "'/>" +
                "</div>");

            console.log("camera name = " + camera.name +", index = " + index);

            return viewItem;
        }

        var i = 0;
        for (var key in cameras) {
            if (cameras.hasOwnProperty(key)) {
                view.append(getView(cameras[key], key));
            }
        }
    }
};