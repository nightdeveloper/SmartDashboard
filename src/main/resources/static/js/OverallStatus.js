OverallStatus = function (divId, batteryData, switchStatus) {

    function getDeviceById(id) {
        for (var i = 0; i < devices.length; i++) {
            if (devices[i].id === id) {
                return devices[i]
            }
        }
        return undefined;
    }

    this.render = function (locationValue) {

        var content = "<h5>Battery status:</h5>";

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

        content += "<h5>Switch status:</h5>";

        for (var id in switchStatus) {

            var status = switchStatus[id];

            var device = getDeviceById(status.deviceId);

            if (!device) {
                return;
            }

            if (locationValue && device.location !== locationValue) {
                continue;
            }



            content += "<p>" + (locationValue ? "" : device.location + ": ") + device.name + " "
                + moment(status.date).format("DD.MM.YYYY") + " ";

            if (status.state === "ON") {
                content += "<span style='color:green'>ON</span>";
            } else if (status.state === "OFF") {
                content += "<span style='color:red'>OFF</span>";
            } else
                content += status.state;

            content += "</p>";
        }

        $("#" + divId).html(content);
    }
};