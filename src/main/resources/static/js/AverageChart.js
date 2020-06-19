AverageChart = function(canvasId, averagesData, title) {

    function newDateFromComponents(year, month, day, hour, minute) {
        var m = moment.utc(year + "-" + month + "-" + day + " " + hour + ":" + minute, "YYYY-MM-DD HH:mm");
        var date = new Date(m.local().format("YYYY-MM-DDTHH:mm:ssZ"));

        return date;
    }

    function momentFromTimestamp(timestamp) {
        var m = moment.utc(timestamp);
        m = m.local();

        return m;
    }

    var config = {
        type: 'line',
        data: {
            datasets: []
        },
        options: {
            responsive: true,
            title: {
                display: true,
                text: title
            },
            scales: {
                xAxes: [{
                    type: 'time',
                    display: true,
                    scaleLabel: {
                        display: false,
                        labelString: 'Date'
                    },
                    ticks: {
                        callback: function(value, index, values) {
                            var timestamp = values[index].value;
                            return momentFromTimestamp(timestamp).format("DD  HH:mm");
                        },
                        major: {
                            fontStyle: 'bold',
                            fontColor: '#FF0000'
                        }
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'value'
                    }
                }]
            }
        }
    };

    function getDeviceById(id) {
        for(var i=0; i<devices.length; i++) {
            if (devices[i].id === id) {
                return devices[i].name
            }
        }
        return id;
    }

    function isDeviceType(id, type) {
        for(var i=0; i<devices.length; i++) {
            if (devices[i].id === id && devices[i].wrappingClass.indexOf(type) > 0) {
                return true;
            }
        }
        return false;
    }

    function getChartColors() {

        var colors = [];

        var chartColors = {
            red: 'rgb(255, 99, 132)',
            green: 'rgb(75, 192, 192)',
            blue: 'rgb(54, 162, 235)',
            purple: 'rgb(153, 102, 255)',
            orange: 'rgb(255, 159, 64)',
            yellow: 'rgb(255, 205, 86)',
            grey: 'rgb(201, 203, 207)'
        };

        var color = Chart.helpers.color;

        colors.push(color(chartColors.red).alpha(0.5).rgbString());
        colors.push(color(chartColors.blue).alpha(0.5).rgbString());
        colors.push(color(chartColors.orange).alpha(0.5).rgbString());
        colors.push(color(chartColors.green).alpha(0.5).rgbString());
        colors.push(color(chartColors.yellow).alpha(0.5).rgbString());
        colors.push(color(chartColors.purple).alpha(0.5).rgbString());

        return colors;
    }

    this.averageChart = averagesData;
    this.canvasId = canvasId;
    this.chart = null;

    var that = this;

    function getChart() {
        return that.chart;
    }

    function fillAverageDatasets() {

        var datasets = config.data.datasets;

        var datasetsByName = {};

        var colors = getChartColors();
        var nextColor = 0;

        for (var i = 0; i < that.averageChart.length; i++) {
            var values = that.averageChart[i];
            var deviceId = values["deviceId"];

            if (!isDeviceType(deviceId, "Comfort")) {
                continue;
            }

            if (!datasetsByName[deviceId]) {
                datasetsByName[deviceId] = {
                    label: getDeviceById(deviceId),
                    backgroundColor: "#FFFFFF",
                    borderColor: colors[nextColor++],
                    fill: false,
                    data: []
                };
            }

            datasetsByName[deviceId].data.push({
                x: newDateFromComponents(values.year, values.month, values.day, values.hour, values.minute),
                y: values.average
            })
        }

        for (var id in datasetsByName) {
            datasetsByName[id].data.sort(function (a, b) {
                if (a.x > b.x) {
                    return 1;
                }

                if (b.x > a.x) {
                    return -1;
                }

                return 0;
            })

            datasets.push(datasetsByName[id]);
        }
    }

    this.render = function() {
        fillAverageDatasets();

        var ctx = document.getElementById(this.canvasId).getContext('2d');

        this.chart = new Chart(ctx, config);
    }
};