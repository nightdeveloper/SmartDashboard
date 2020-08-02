function createSensorsNavigation() {

    var locations = [];

    devices.forEach(value => {
        if (value.location && !locations.includes(value.location)) {
            locations.push(value.location);
        }
    })

    var nav = $("#content_nav_sensors");

    nav.append("<div class='navLabel'>Locations:</div>");


    function getNavLocationElement(value) {
        var navLocation = $("<a class=\"nav-link\" href=\"#sensors\" data-value=\"" + value + "\">" + value + "</a>");

        navLocation.click(function () {
            window.updateSensorsNavigationLocation(value);
        });

        return navLocation;
    }

    nav.append(getNavLocationElement("all"));

    locations.forEach(value => {
        nav.append(getNavLocationElement(value));
    });

}

window.onload = function () {

    var navigation = new Navigation();
    navigation.init();

    createSensorsNavigation();

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

    var camerasTab = new CamerasTab();
    camerasTab.init();

    var discoveryTab = new DiscoveryTab();
    discoveryTab.init();

    window.updateSensorsNavigationLocation = function(locationValue) {

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