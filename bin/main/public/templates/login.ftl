<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Login</title>

    <!-- Custom fonts for this template-->
    <link href="../assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Custom styles for this template-->
    <link href="../assets/css/sb-admin.css" rel="stylesheet">

</head>

<body class="bg-dark">

<div class="container">
    <div class="card card-login mx-auto mt-5">
        <div class="card-header">Login
        </div>
        <div class="card-body text-center">
            <a class="navbar-brand mr-1" href="/"><img src="../assets/img/chinde-logo.gif" alt="animated"/></a>
            <form action="/hacerLogin/" method="post"  enctype="application/x-www-form-urlencoded">
                <div class="form-group">
                    <div class="form-label-group">
                        <input name="username" class="form-control" placeholder="Username" type="username"
                               id="username" required="required" autofocus="autofocus">
                        <label for="username">Username</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-label-group">
                        <input name="password" class="form-control" placeholder="password" type="password"
                               id="password" required="required" autofocus="autofocus">
                        <label for="password">Password</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <input name="recordar" id="recordar" value="remember-me" type="checkbox" >
                            Remember Me
                        </label>
                    </div>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Login</button>
            </form>
            <div class="text-center">
<#--                <div class="col justify-content-end"><a href="/register" class="float-right btn btn-outline-primary">Sign up</a></div>-->
                <a href="/register" class="d-block small mt-3" href="register.html">Register an Account</a>
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
