
  var changeTab = function(index) {
      var headTabDom = $api.domAll('.head-tab li');
      for (var i = 0; i < headTabDom.length; i++) {
          $api.removeCls(headTabDom[i], 'active');
          $api.addCls($api.byId('tab' + i), 'hide');
      };
      $api.addCls(headTabDom[index], 'active');
      $api.removeCls($api.byId('tab' + index), 'hide');
      window.scrollTo(0, 0);
      $api.addCls($api.byId('res'), 'hide');
  }
  var init = function() {
      var opts = {
          lines: 12, // The number of lines to draw
          angle: 0, // The length of each line
          lineWidth: 0.48, // The line thickness
          pointer: {
              length: 0.3, // The radius of the inner circle
              strokeWidth: 0.03, // The rotation offset
              color: '#00C8FF' // Fill color
          },
          limitMax: 'true', // If true, the pointer will not go past the end of the gauge
          colorStart: '#0094FF',
          colorStop: '#0094FF', // just experiment with them
          strokeColor: '#E5E5E5',
          generateGradient: true
      };
      var gauge = new Gauge($api.byId('chart-zdl')).setOptions(opts); // create sexy gauge!
      gauge.maxValue = 3000; // set max gauge value
      gauge.animationSpeed = 32; // set animation speed (32 is default value)
      gauge.set(1150);

      var randomScalingFactor = function() {
          return Math.round(Math.random() * 100)
      };
      var lineChartData = {
          labels: ["01", "02", "03", "04"],
          datasets: [{
              label: "01",
              fillColor: "rgba(5,159,178,1)",
              strokeColor: "rgba(5,159,178,1)",
              pointColor: "rgba(5,159,178,1)",
              pointStrokeColor: "rgba(5,159,178,1)",
              pointHighlightFill: "rgba(5,159,178,1)",
              pointHighlightStroke: "rgba(5,159,178,1)",
              data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
          }, {
              label: "02",
              fillColor: "rgba(253,113,24,1)",
              strokeColor: "rgba(253,113,24,1)",
              pointColor: "rgba(253,113,24,1)",
              pointStrokeColor: "rgba(253,113,24,1)",
              pointHighlightFill: "rgba(253,113,24,1)",
              pointHighlightStroke: "rgba(253,113,24,1)",
              data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
          }, {
              label: "03",
              fillColor: "rgba(31,91,162,1)",
              strokeColor: "rgba(31,91,162,1)",
              pointColor: "rgba(31,91,162,1)",
              pointStrokeColor: "rgba(31,91,162,1)",
              pointHighlightFill: "rgba(31,91,162,1)",
              pointHighlightStroke: "rgba(31,91,162,1)",
              data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
          }]

      }
      var xxdl_ctx = $api.byId("xxdl-chart").getContext("2d");
      window.xxdl_line = new Chart(xxdl_ctx).Line(lineChartData, {
          scaleShowVerticalLines: false,
          datasetStroke: false,
          pointDot: false,
          datasetStroke: false,
      });
      var ssqx_ctx = $api.byId("ssqx-chart").getContext("2d");
      window.ssqx_line = new Chart(ssqx_ctx).Line(lineChartData, {
          scaleShowVerticalLines: false,
          datasetStroke: false,
          pointDot: false,
          datasetStroke: false,
      });
  }
