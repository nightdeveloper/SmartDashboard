CurrencyChart = function(canvasId, currencyRates, title) {

    var movingAverageValues = 15;

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

    this.trendLine = function(dataset) {
        var origData = dataset.data;

        var intervalData = [];

        var maxPeak = undefined;
        var minPeak = undefined;
        for (var i = 1; i < origData.length - 1; i++) {
            var newPoint = {
                x: origData[i].x,
                y: origData[i].y
            }

            // detect max peak
            if (origData[i+1].y > origData[i].y) {
                maxPeak = origData[i+1].y;
            } else if ((origData[i+1].y < origData[i].y) && (typeof maxPeak === 'number')) {
                newPoint.isMax = true;
                maxPeak = undefined;
            }

            // detect min peak
            if (origData[i+1].y < origData[i].y) {
                minPeak = origData[i+1].y;
            } else if ((origData[i+1].y > origData[i].y) && (typeof minPeak === 'number')) {
                newPoint.isMin = true;
                minPeak = undefined;
            }

            if (newPoint.isMin || newPoint.isMax) {
                intervalData.push(newPoint);
            }

            console.log(newPoint.y + " " + (newPoint.isMin? "min": "") + (newPoint.isMax? "max" : ""));
        }

        return intervalData;
    }

    this.movingAvg = function(dataset) {
        var origData = dataset.data;

        var keys = [];
        var values = [];

        for (var i = origData.length - 1; i >= 0; i--) {
            keys.push(origData[i].x);
            values.push(origData[i].y);
        }

        var movingAvg = [];
        while (values.length > movingAverageValues) {
            avg = R.pipe(R.take(movingAverageValues), R.sum)(values) / movingAverageValues;
            values = values.slice(1);
            movingAvg.push(avg.toFixed(4));
        }

        keys = R.take(keys.length - movingAverageValues)(keys);

        var mappedIndex = R.addIndex(R.map);
        var data = mappedIndex((val, i) => {
            return {
                x: keys[i],
                y: val
            }
        })(movingAvg);

        return data;
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

            datasets.push({
                label: id + " MA (" + movingAverageValues + ")",
                backgroundColor: "#FFFFFF",
                borderColor: colors[nextColor++],
                fill: false,
                borderDash: [5, 5],
                data: this.movingAvg(datasetsByName[id])
            });

            /*
            datasets.push({
                label: id + " trend line",
                backgroundColor: "#FFFFFF",
                borderColor: colors[nextColor++],
                fill: false,
                data: this.trendLine(datasetsByName[id])
            });
             */
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