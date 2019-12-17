<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Dashboard</title>

    <!-- Custom fonts for this template-->
    <link href="../assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Page level plugin CSS-->
    <link href="../assets/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="../assets/css/sb-admin.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/combine/npm/react@16/umd/react.production.min.js,npm/react-dom@16/umd/react-dom.production.min.js,npm/styled-components@4/dist/styled-components.min.js,npm/@microlink/mql@latest/dist/mql.min.js,npm/@microlink/vanilla@latest/dist/microlink.min.js"></script><script src="https://cdn.jsdelivr.net/combine/npm/react@16/umd/react.production.min.js,npm/react-dom@16/umd/react-dom.production.min.js,npm/styled-components@4/dist/styled-components.min.js,npm/@microlink/mql@latest/dist/mql.min.js,npm/@microlink/vanilla@latest/dist/microlink.min.js"></script>

</head>

<body id="page-top">

<#include "admin-navbar.ftl">

<div id="wrapper">
    <#include "admin-sidebar.ftl">

    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Breadcrumbs-->
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="#">Dashboard</a>
                </li>
                <li class="breadcrumb-item active">Overview</li>
            </ol>

            <!-- Icon Cards-->
            <!-- DataTables Example -->
            <div class="card mb-3">
                <div class="card-header">
                    <i class="fas fa-table"></i>
                    Mi Contenido</div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                            <tr>
                                <th>URL Original</th>
                                <th>URL Acortada</th>
                                <th>Username</th>
                                <th>Fecha Creación</th>
                                <th>Fecha ultimo Acceso</th>
                                <th>Info link</th>
                                <th>Impacto</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <td>URL Original</td>
                                    <td>URL Acortada</td>
                                    <td>Username</td>
                                    <td>Fecha Creación</td>
                                    <td>Fecha ultimo Acceso</td>
                                    <td>Info link</td>
                                    <td>Impacto</td>
                                    <td>Acciones</td>
                                </tr>
                            </tfoot>
                            <tbody>
                                <#if links?size != 0>
                                    <#list links as link>
                                        <tr>
                                            <td>
                                                <#if link.urlReferencia?length &gt; 72>
                                                    <a href="${link.urlReferencia}">${link.urlReferencia?substring(0,72)} ...</a>
                                                    <#else>
                                                        <a href="${link.urlReferencia}">${link.urlReferencia}</a>
                                                </#if>
                                            </td>
                                            <td><a href="${link.urlGenerada}">${link.urlGenerada}</a></td>
                                            <td>${link.usuario.username}</td>
                                            <td>${link.fechaCreacion}</td>
                                            <td>${link.getTopAccesoFechaMasReciente()}</td>
                                            <td>
                                                <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#modalQR${link.id}">
                                                    Info link
                                                </button>
                                                <!-- Modal -->
                                                <div class="modal fade" id="modalQR${link.id}" tabindex="-1" role="dialog" aria-labelledby="modalQR${link.id}Title" aria-hidden="true">
                                                    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="modalQR${link.id}Title">Información QR</h5>
                                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <div class="card-img" id="qrcode${link.urlGenerada}"></div>
                                                            </div>
                                                            <div class="col-md-8">
                                                                <div class="modal-body">
                                                                    <#--                                                                <div class="row">-->
                                                                    <#--                                                                    <div class="col">-->
                                                                    <#--                                                                        <div id="qrcode${link.urlGenerada}"></div>-->
                                                                    <#--                                                                    </div>-->
                                                                    <#--                                                                </div>-->
                                                                    <#--                                                                <div class="row">-->
                                                                    <#--                                                                    <h6><a class="link-previews" href="${link.urlReferencia}">${link.urlReferencia}</a></h6>-->
                                                                    <#--                                                                </div>-->
                                                                    <#if link.urlReferencia?length &gt; 55>
                                                                        <div class="row">
                                                                            <h4 class="card-text text-black-50"><strong>Link de referencia</strong></h4>
                                                                        </div>
                                                                        <div class="row">
                                                                            <h5> <a class="link-previews" href="${link.urlReferencia}">${link.urlReferencia?substring(0,55)} ...</a></h5>
                                                                        </div>
                                                                    <#else>
                                                                        <div class="row">
                                                                            <h4 class="card-text text-black-50"><strong>Link de referencia</strong></h4>
                                                                        </div>
                                                                        <div class="row">
                                                                            <h5><a class="link-previews" href="${link.urlReferencia}">${link.urlReferencia}</a></h5>
                                                                        </div>
                                                                    </#if>
                                                                    <br/>
                                                                    <div class="row">
                                                                        <h4 class="card-text text-black-50"><strong>Link Generado</strong></h4>
                                                                    </div>
                                                                    <br/>
                                                                    <div class="row">
                                                                        <h5 class="card-text"><a class="alert alert-primary" href="${link.urlGenerada}">${link.urlGenerada}</a></h5>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
<#--                                            TODO: Agregar modal. Probar primero en el home.-->
                                            <td><a href="/campaignStatistics/${link.id}">
                                                    Reportes
                                                </a>
                                            </td>
                                            <td class="text-center">
                                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#eliminarUrlModal${link.id}">
                                                    Eliminar
                                                </button>
                                                <!-- Modal -->
                                                <div class="modal fade" id="eliminarUrlModal${link.id}" tabindex="-1" role="dialog" aria-labelledby="eliminarUrlTitle" aria-hidden="true">
                                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="eliminarUrlTitole">Modal title</h5>
                                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <strong>¿Esta seguro que desea borrar la URL?</strong>
                                                                <p class="alert alert-danger"><strong>${link.urlReferencia}</strong></p>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                <form method="post" action="/eliminarURL/${link.id}">
                                                                    <button type="submit" class="btn btn-danger text-white">Eliminar</button>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </#list>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
            </div>
            <!-- Area Chart Example-->
            <div class="card mb-3">
                <div class="card-header">
                    <i class="fas fa-chart-area"></i>
                    Cantidad de visitas por Hora</div>
                <div class="card-body">
                    <canvas id="myHoursChart" width="100%" height="30"></canvas>
                </div>
                <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
            </div>
            <div class="card mb-3">
                <div class="card-header">
                    <i class="fas fa-chart-bar"></i>
                    Cantidad de visitas por Dia</div>
                <div class="card-body">
                    <canvas id="myBarChartDays" width="100%" height="50"></canvas>
                </div>
                <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
            </div>

            <div class="row">
                <div class="col-lg-6">
                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fas fa-chart-pie"></i>
                            Cantidad de visitas por Explorador</div>
                        <div class="card-body">
                            <canvas id="myBrowserPieChart" width="100%" height="100"></canvas>
                        </div>
                        <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fas fa-chart-pie"></i>
                            Cantidad de visitas por Sistema Operativo</div>
                        <div class="card-body">
                            <canvas id="myOsPiChart" width="100%" height="100"></canvas>
                        </div>
                        <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Sticky Footer -->
        <footer class="sticky-footer">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright © Your Website 2019</span>
                </div>
            </div>
        </footer>
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- /#wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="login.html">Logout</a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="../assets/vendor/jquery/jquery.min.js"></script>
<script src="../assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="../assets/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Page level plugin JavaScript-->
<script src="../assets/vendor/chart.js/Chart.min.js"></script>
<script src="../assets/vendor/datatables/jquery.dataTables.js"></script>
<script src="../assets/vendor/datatables/dataTables.bootstrap4.js"></script>
<script src="../js/qrcode.min.js"></script>
<#if links?size != 0>
    <#list links as link>
        <script type="text/javascript">
            new QRCode(document.getElementById("qrcode${link.urlGenerada}"), "chinde.team/${link.urlGenerada}");
            var qrcode = new QRCode("test", {
                text: "chinde.team/${link.urlGenerada}",
                width: 128,
                height: 128,
                colorDark : "#000000",
                colorLight : "#ffffff",
                correctLevel : QRCode.CorrectLevel.H
            });
        </script>
    </#list>
</#if>
<script>
    document.addEventListener('DOMContentLoaded', function (event) {
        microlink('.link-previews', {
            size: 'small'
        })
    })
</script>
<!-- Custom scripts for all pages-->
<script src="../assets/js/sb-admin.min.js"></script>
<!-- Demo scripts for this page-->
<script src="../assets/js/demo/datatables-demo.js"></script>
    <script>
        Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,' +
            '"Helvetica Neue",Arial,sans-serif';
        Chart.defaults.global.defaultFontColor = '#292b2c';

        // Pie Chart Example
        var ctx = document.getElementById("myBrowserPieChart");
        var myPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["Chrome", "Safari", "Opera", "Firefox", "Edge"],
                datasets: [{
                    data: [${cc}, ${cs}, ${co}, ${ce}, ${cf}, ${cb}],
                    backgroundColor: ['#dc3545','#007bff', '#98412E', '#94EC1B', '#FAF90F', '#F8A500'],
                }],
            },
        });
    </script>
    <script>
        Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
        Chart.defaults.global.defaultFontColor = '#292b2c';

        // Pie Chart Example
        var ctx = document.getElementById("myOsPiChart");
        var myPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["Android", "iOS", "MacOS", "Linux", "Ubuntu", "Windows"],
                datasets: [{
                    data: [${and}, ${io}, ${mo}, ${li}, ${ub}, ${wi}],
                    backgroundColor: ['#dc3545','#007bff', '#98412E', '#94EC1B', '#FAF90F', '#F8A500'],
                }],
            },
        });
    </script>

    <script>
        // Set new default font family and font color to mimic Bootstrap's default styling
        Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
        Chart.defaults.global.defaultFontColor = '#292b2c';

        // Area Chart Example
        var ctx = document.getElementById("myHoursChart");
        var myLineChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00","16:00", "17:00"
                    , "18:00", "19:00", "20:00", "21:00","22:00", "23:00"],
                datasets: [{
                    label: "Visitas por Hora",
                    lineTension: 0.3,
                    backgroundColor: "rgba(2,117,216,0.2)",
                    borderColor: "rgba(2,117,216,1)",
                    pointRadius: 5,
                    pointBackgroundColor: "rgba(2,117,216,1)",
                    pointBorderColor: "rgba(255,255,255,0.8)",
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: "rgba(2,117,216,1)",
                    pointHitRadius: 50,
                    pointBorderWidth: 2,
                    data: [${zero}, ${one}, ${two}, ${three}, ${four}, ${five}, ${six}, ${seven}, ${eight}, ${nine}, ${ten},
                        ${eleven}, ${twelve}, ${thirteen}, ${fourteen}, ${fifteen}, ${sixteen}, ${seventeen}, ${eighteen},
                        ${nineteen}, ${twenty}, ${twenty_one}, ${twenty_two}, ${twenty_three}],
                }],
            },
            options: {
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'date'
                        },
                        gridLines: {
                            display: false
                        },
                        ticks: {
                            maxTicksLimit: 7
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 15,
                            maxTicksLimit: 5
                        },
                        gridLines: {
                            color: "rgba(0, 0, 0, .125)",
                        }
                    }],
                },
                legend: {
                    display: false
                }
            }
        });

    </script>
    <script>
        Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
        Chart.defaults.global.defaultFontColor = '#292b2c';

        // Bar Chart Example
        var ctx = document.getElementById("myBarChartDays");
        var myLineChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                datasets: [{
                    label: "Visitas",
                    backgroundColor: "rgba(2,117,216,1)",
                    borderColor: "rgba(2,117,216,1)",
                    data: [${mon}, ${tue}, ${wen}, ${thu}, ${fri}, ${sat}, ${sun}],
                }],
            },
            options: {
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'day of week'
                        },
                        gridLines: {
                            display: false
                        },
                        ticks: {
                            maxTicksLimit: 7
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 15,
                            maxTicksLimit: 5
                        },
                        gridLines: {
                            display: true
                        }
                    }],
                },
                legend: {
                    display: false
                }
            }
        });
    </script>

</body>

</html>
