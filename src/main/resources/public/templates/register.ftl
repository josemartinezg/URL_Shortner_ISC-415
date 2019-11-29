<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Register</title>

    <!-- Custom fonts for this template-->
    <link href="../assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Custom styles for this template-->
    <link href="../assets/css/sb-admin.css" rel="stylesheet">

</head>

<body class="bg-dark">

<div class="container">
    <div class="card card-register mx-auto mt-5">
        <div class="card-header">Register an Account</div>
        <div class="card-body">
            <form action="/hacerRegister/" method="post"  enctype="application/x-www-form-urlencoded">
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
                <button class="btn btn-primary btn-block" type="submit">Register</button>
            </form>
            <div class="text-center">
                <a class="d-block small mt-3">Login Page</a>
                <a class="d-block small" href="forgot-password.html">Forgot Password?</a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="../assets/vendor/jquery/jquery.min.js"></script>
<script src="../assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="../assets/vendor/jquery-easing/jquery.easing.min.js"></script>

</body>

</html>
