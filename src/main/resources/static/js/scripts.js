window.onload = function () {

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
};