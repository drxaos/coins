function HomeCtrl($location, AuthService, $rootScope, $http) {
    var model = this;

    AuthService.checkLoggedIn().then(function () {

        var chart = new Highcharts.StockChart({
            chart: {
                renderTo: 'ChartsStockContainer'
            },

            title: {
                text: 'Stock Chart'
            },

            subtitle: {
                text: 'test'
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
                                    headingText: "Transactions",
                                    maincontentText: Highcharts.dateFormat('%A, %b %e, %Y', this.x) + ':<br/> ' +
                                    this.y + ' RUR',
                                    width: 200
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
                name: 'Balance',
                data: [],
                lineWidth: 4,
                marker: {
                    radius: 4
                }
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
