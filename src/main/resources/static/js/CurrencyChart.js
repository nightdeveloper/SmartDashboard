CurrencyChart = function(canvasId, currencyRates, title) {

    var config = {
        type: 'line',
        data: {
            datasets: []
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            title: {
                display: true,
                text: title
            },
            scales: {
                xAxes: [{
                    type: 'time',
                    display: true,
                    time: {
                        tooltipFormat:'MM.DD.YYYY HH:mm'
                    },
                    scaleLabel: {
                        display: false,
                        labelString: 'Date'
                    },
                    ticks: {
                        callback: function(value, index, values) {
                            return values[index].date;
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

    this.currencyRates = currencyRates;
    this.canvasId = canvasId;
    this.chart = null;

    var that = this;

    function getChart() {
        return that.chart;
    }

    this.fillAverageDatasets = function(locationFilter) {

        var datasets = [];

        var datasetsByName = {};

        var colors = getChartColors();
        var nextColor = 0;

        for (var i = 0; i < that.currencyRates.length; i++) {
            var rate = that.currencyRates[i];

            var charCode = rate.charCode;
            var value = rate.value;
            var date = rate.date;

            if (!datasetsByName[charCode]) {
                datasetsByName[charCode] = {
                    label: charCode,
                    backgroundColor: "#FFFFFF",
                    borderColor: colors[nextColor++],
                    fill: false,
                    data: []
                };
            }

            datasetsByName[charCode].data.push({
                x: date,
                y: value
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

        config.data.datasets = datasets;
    }

    this.render = function() {
        this.fillAverageDatasets();

        var ctx = document.getElementById(this.canvasId).getContext('2d');

        this.chart = new Chart(ctx, config);
    }

    this.update = function() {
        this.chart.update();
    }
};