BatteryStatus = function (divId, batteryData, title) {

    function getDeviceById(id) {
        for (var i = 0; i < devices.length; i++) {
            if (devices[i].id === id) {
                return devices[i]
            }
        }
        return undefined;
    }

    this.render = function (locationValue) {

        var content = "<h5>" + title + ":</h5>";

        for (var id in batteryData) {

            if (!batteryData.hasOwnProperty(id)) {
                continue;
            }

            var data = batteryData[id];

            var device = getDeviceById(id);

            if (!device) {
                return;
            }

            if (locationValue && device.location !== locationValue) {
                continue;
            }

            content += "<p>" + (locationValue ? "" : device.location + ": ") + device.name + " "
                + moment(data.currentValueDate).format("DD.MM.YYYY") + " "
                + data.currentValue + "% ";

            if (data.daysLeft > 0) {
                content += ", est. " + data.daysLeft + " days left"
            }

            content += "</p>";
        }

        $("#" + divId).html(content);
    }
};