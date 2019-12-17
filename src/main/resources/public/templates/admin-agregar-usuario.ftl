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
            <div class="col-md-6">
                <div class="card mb-3">
                    <div class="card-header">
                        <i class="fas fa-table"></i>
                        Agregar usuario
                    </div>
                    <div class="card-body">
                        <form action="/hacerRegisterAdmin/" method="post"  enctype="application/x-www-form-urlencoded">
                            <div class="form-group">
                                <div class="form-row">
                                    <div class="col-md-6">
                                        <div class="form-label-group">
                                            <input name="nombre" class="form-control" placeholder="Nombre" type="text"
                                                   id="nombre" required="required" autofocus="autofocus">
                                            <label for="nombre">Nombre</label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-label-group">
                                            <input name="apellido" class="form-control" placeholder="Apellido" type="text"
                                                   id="apellido" required="required" autofocus="autofocus">
                                            <label for="apellido">Apellido</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-label-group">
                                    <input name="username" class="form-control" placeholder="username" type="text"
                                           id="username" required="required" autofocus="autofocus">
                                    <label for="username">Username</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-row">
                                    <div class="col-md-6">
                                        <div class="form-label-group">
                                            <input name="password" class="form-control" placeholder="******" type="password"
                                                   id="password" required="required">
                                            <label for="password">Password</label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-label-group">
                                            <input name="repassword" class="form-control" placeholder="******" type="password"
                                                   id="repassword" required="required">
                                            <label for="repassword">Re-Type Password</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-row text-center">
                                    <div class="col">
                                        <div class="custom-control custom-checkbox">
                                            <input name="isadmin" type="checkbox" class="custom-control-input" id="customControlInline">
                                            <label class="custom-control-label" for="customControlInline">Is the user Admin?</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-8">
                                    <button class="btn btn-primary btn-block" type="submit">Register User</button>
                                </div>
                                <div class="col-4">
                                    <span class="btn btn-danger btn-block">
                                        <a class="text-white" href="/usuarios">Cancel</a>
                                    </span>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
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

<!-- Custom scripts for all pages-->
<script src="../assets/js/sb-admin.min.js"></script>

<!-- Demo scripts for this page-->
<script src="../assets/js/demo/datatables-demo.js"></script>


</body>

</html>
