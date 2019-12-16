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
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Username</th>
                                <td>Is Admin?</td>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tfoot>
                            <tr>
                                <td>URL Original</td>
                                <td>URL Acortada</td>
                                <td>Username</td>
                                <td>Is Admin?</td>
                                <td>Acciones</td>
                            </tr>
                            </tfoot>
                            <tbody>
                            <#if usuarios?size != 0>
                                <#list usuarios as usuario>
                                    <tr>
                                        <td>${usuario.username}</td>
                                        <td>${usuario.nombre}</td>
                                        <td>${usuario.apellido}</td>
<#--                                        <td>${usuario.administrator}</td>-->
                                        <td class="text-center">
                                            <button type="button" class="btn btn-danger" data-toggle="modal" >
<#--                                                data-target="#eliminarUrlModal${link.id}-->
                                                Eliminar
                                            </button>
<#--                                            <!-- Modal &ndash;&gt;-->
<#--                                            <div class="modal fade" id="eliminarUrlModal${}" tabindex="-1" role="dialog" aria-labelledby="eliminarUrlTitle" aria-hidden="true">-->
<#--                                                <div class="modal-dialog modal-dialog-centered" role="document">-->
<#--                                                    <div class="modal-content">-->
<#--                                                        <div class="modal-header">-->
<#--                                                            <h5 class="modal-title" id="eliminarUrlTitole">Modal title</h5>-->
<#--                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">-->
<#--                                                                <span aria-hidden="true">&times;</span>-->
<#--                                                            </button>-->
<#--                                                        </div>-->
<#--                                                        <div class="modal-body">-->
<#--                                                            <strong>¿Esta seguro que desea borrar la URL?</strong>-->
<#--                                                            <p class="alert alert-danger"><strong>${link.urlReferencia}</strong></p>-->
<#--                                                        </div>-->
<#--                                                        <div class="modal-footer">-->
<#--                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>-->
<#--                                                            <form method="post" action="/eliminarURL/${link.id}">-->
<#--                                                                <button type="submit" class="btn btn-danger text-white">Eliminar</button>-->
<#--                                                            </form>-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
<#--                                            </div>-->
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

<!-- Custom scripts for all pages-->
<script src="../assets/js/sb-admin.min.js"></script>

<!-- Demo scripts for this page-->
<script src="../assets/js/demo/datatables-demo.js"></script>

</body>

</html>
