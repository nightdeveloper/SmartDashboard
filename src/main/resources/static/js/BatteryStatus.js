BatteryStatus = function (divId, batteryData, title) {

    function getDeviceById(id) {
        for (var i = 0; i < devices.length; i++) {
            if (devices[i].id === id) {
                return devices[i].name
            }
        }
        return id;
    }

    this.render = function () {

        var content = "<h5>" + title + ":</h5>";

        for (var id in batteryData) {

            if (!batteryData.hasOwnProperty(id)) {
                continue;
            }

            var data = batteryData[id];

            content += "<p>" + getDeviceById(id) + " "
                + moment(data.currentValueDate).format("DD.MM.YYYY") + " "
                + data.currentValue + "% ";

            if (data.daysLeft > 0) {
                content += ", " + data.daysLeft + " days left"
            }

            content += "</p>";
        }

        $("#" + divId).html(content);
    }
};