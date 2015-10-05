function HomeCtrl($location, AuthService, $rootScope, $http, $translate) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        Highcharts.setOptions({
            lang: {
                shortMonths: [
                    $translate.instant('MONTHS_SHORT_01'),
                    $translate.instant('MONTHS_SHORT_02'),
                    $translate.instant('MONTHS_SHORT_03'),
                    $translate.instant('MONTHS_SHORT_04'),
                    $translate.instant('MONTHS_SHORT_05'),
                    $translate.instant('MONTHS_SHORT_06'),
                    $translate.instant('MONTHS_SHORT_07'),
                    $translate.instant('MONTHS_SHORT_08'),
                    $translate.instant('MONTHS_SHORT_09'),
                    $translate.instant('MONTHS_SHORT_10'),
                    $translate.instant('MONTHS_SHORT_11'),
                    $translate.instant('MONTHS_SHORT_12'),
                ],
                weekdays: [
                    $translate.instant('WEEKDAY_SHORT_1'),
                    $translate.instant('WEEKDAY_SHORT_2'),
                    $translate.instant('WEEKDAY_SHORT_3'),
                    $translate.instant('WEEKDAY_SHORT_4'),
                    $translate.instant('WEEKDAY_SHORT_5'),
                    $translate.instant('WEEKDAY_SHORT_6'),
                    $translate.instant('WEEKDAY_SHORT_7'),
                ]
            }
        });

        var chart = new Highcharts.StockChart({
            chart: {
                renderTo: 'ChartsStockContainer'
            },

            title: {
                text: $translate.instant('HOME_CHART_TITLE')
            },

            tooltip: {
                shared: true,
                crosshairs: true
            },

            rangeSelector: {
                selected: 1
            },

            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 3,
                    color: 'red'
                }]
            },

            plotOptions: {
                series: {
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (e) {
                                hs.htmlExpand(null, {
                                    pageOrigin: {
                                        x: e.pageX || e.clientX,
                                        y: e.pageY || e.clientY
                                    },
                                    headingText: Highcharts.dateFormat('%A, %b %e, %Y', this.x) + ': ' + this.y + ' ' + $translate.instant('HOME_CHART_RUR'),
                                    maincontentText: "tx...",
                                    width: 300
                                });
                            }
                        }
                    },
                    marker: {
                        lineWidth: 2
                    }
                }
            },

            series: [{
                name: $translate.instant('HOME_CHART_BALANCE'),
                data: [],
                lineWidth: 3,
                marker: {
                    enabled: true,
                    radius: 5
                },
                shadow: true,
            }]
        });

        $http.get('/api/v1/charts/stock')
            .success(function (data) {
                chart.series[0].setData(data);
            });

        $rootScope.toolbarTools = [];
        $rootScope.fab = {show: false};

    });
}

InitializingModule.controller('HomeCtrl', HomeCtrl);
