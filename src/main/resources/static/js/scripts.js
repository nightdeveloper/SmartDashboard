function createNavigation() {

    var locations = [];

    devices.forEach(value => {
        if (value.location && !locations.includes(value.location)) {
            locations.push(value.location);
        }
    })

    var nav = $(".nav");

    nav.append("<div class='navLabel'>Locations:</div>");


    function getNavLocationElement(value) {
        var navLocation = $("<a class=\"nav-link\" href=\"#\" data-value=\"" + value + "\">" + value + "</a>");

        navLocation.click(function () {
            window.updateNavigationLocation(value);
        });

        return navLocation;
    }

    nav.append(getNavLocationElement("all"));

    locations.forEach(value => {
        nav.append(getNavLocationElement(value));
    });

}

window.onload = function () {

    createNavigation();

    var temperatureChart = new AverageChart("temperaturesCanvas", telemetryTemperatures, "Temperature averages");
    temperatureChart.render();

    var humidityChart = new AverageChart("humidityCanvas", telemetryHumidity, "Humidity averages");
    humidityChart.render();

    var pressureChart = new AverageChart("pressureCanvas", telemetryPressure, "Pressure averages");
    pressureChart.render();

    var linkQualityChart = new AverageChart("linkQualityCanvas", telemetryLinkQuality, "Link quality averages");
    linkQualityChart.render();

    var batteryChart = new AverageChart("batteryCanvas", telemetryBattery, "Battery chart");
    batteryChart.render();

    var batteryStatus = new BatteryStatus("batteryStatus", telemetryBatteryStatus, "Battery status");
    batteryStatus.render();

    window.updateNavigationLocation = function(locationValue) {

        var val = locationValue === "all" ? undefined : locationValue;

        temperatureChart.fillAverageDatasets(val);
        temperatureChart.update();

        humidityChart.fillAverageDatasets(val);
        humidityChart.update();

        pressureChart.fillAverageDatasets(val);
        pressureChart.update();

        linkQualityChart.fillAverageDatasets(val);
        linkQualityChart.update();

        batteryChart.fillAverageDatasets(val);
        batteryChart.update();

        batteryStatus.render(val);
    }
};