<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Traffic Monitor</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-12 text-center">
            <label class="text-dark fs-1 fw-bold">- Traffic Monitor Dashboard -</label>
        </div>
    </div>
    <div class="row">
        <canvas class="col-12 mt-5" id="chart-div" style="height: 500px">
        </canvas>
    </div>
    <div class="row mt-5 mb-3">
        <div class="col-12">
            <label class="text-danger fs-2" id="high-traffic"></label>
        </div>
    </div>
    <div class="row mb-4 mt-2">
        <div class="col-5 mt-3">
            <div class="row gy-2" id="full-route-data"></div>
        </div>
        <div class="col-3">
            <div class="row">
                <div class="col-12">
                    <label class="fs-3 fw-bold text-center">Start Side</label>
                </div>
                <div class="col-12">
                    <canvas id="start-chart"></canvas>

                </div>
            </div>
        </div>
        <div class="col-3 offset-1">
            <div class="row">
                <div class="col-12">
                    <label class="fs-3 fw-bold text-center">End Side</label>
                </div>
                <div class="col-12">
                    <canvas id="end-chart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.min.js"
        integrity="sha512-L0Shl7nXXzIlBSUUPpxrokqq4ojqgZFQczTYlGjzONGTDAcLremjwaWv5A+EDLnxhQzY5xUZPWLOLqYRkY0Cbw=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"
        integrity="sha512-CQBWl4fJHWbryGE+Pc7UAxWMUMNMWzWxF4SQo9CgkJIN1kx6djDQZjh3Y8SZ1d+6I+1zze6Z7kHXO7q3UyZAWw=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/helpers.min.js"
        integrity="sha512-JG3S/EICkp8Lx9YhtIpzAVJ55WGnxT3T6bfiXYbjPRUoN9yu+ZM+wVLDsI/L2BWRiKjw/67d+/APw/CDn+Lm0Q=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<script>
    const ctx = document.getElementById('chart-div');

    let chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Average Speed',
                data: [],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    setInterval(()=>{
        fetch('graph', {
            method:'get',
        }).then(response =>response.json())
            .then(json=>{
                if(chart.data.labels.length > 20){
                    removeData(chart);
                }
                let time = json.time.split('.');
                addData(chart,json.avgSpeed,time[0])
            })

    },1000*10);

    function addData(chart, newData, label) {
        chart.data.labels.push(label);
        chart.data.datasets.forEach((dataset) => {
            dataset.data.push(newData);
        });
        chart.update();
    }

    function removeData(chart) {
        chart.data.labels.shift();
        chart.data.datasets.forEach((dataset) => {
            dataset.data.shift();
        });
        chart.update();
    }

    setInterval(()=>{

    },1000*60);

    fetch('analyze',{
        method : 'get',
    }).then(response => response.json())
        .then(json=>{
           document.getElementById('high-traffic').innerHTML = 'High traffic detected on <b>'+json.highTrafficRoute+'</b> Route';

           console.log(json.countByRoute);
           let obj = json.countByRoute;
           let routeDiv = document.getElementById('full-route-data');
           for(let key in obj){
               console.log(key.length)
               let topic = key+' ';
               for(let i = key.length; i < 75;i++){
                   topic += '-'
               }
                let keyDiv = document.createElement('div');
                keyDiv.className = 'col-9';
                keyDiv.innerHTML = topic;
               let valueDiv = document.createElement('div');
               valueDiv.className = 'col-3';
               valueDiv.innerHTML = obj[key];
               routeDiv.appendChild(keyDiv);
               routeDiv.appendChild(valueDiv);
           }

           console.log("Count: "+json.vehicleCount['startRight'])

            let chartContext = document.getElementById('start-chart');
            let polerChart = new Chart(chartContext, {
                type: 'pie',
                data: {
                    labels: [
                        'North Started',
                        'South Started',
                        'Right Started',
                        'Left Started'
                    ],
                    datasets: [{
                        label: 'Vehicle count',
                        data: [json.vehicleCount['startNorth'], json.vehicleCount['startSouth'], json.vehicleCount['startRight'],json.vehicleCount['startLeft']],
                        hoverOffset: 4
                    }]
                },
            });

            let endChartContext = document.getElementById('end-chart');
            let endChart = new Chart(endChartContext, {
                type: 'pie',
                data: {
                    labels: [
                        'North End',
                        'South End',
                        'Right End',
                        'Left End'
                    ],
                    datasets: [{
                        label: 'Vehicle count',
                        data: [json.vehicleCount['endNorth'], json.vehicleCount['endSouth'], json.vehicleCount['endRight'],json.vehicleCount['endLeft']],
                        hoverOffset: 4
                    }]
                },
            });
        });

</script>

</body>
</html>
