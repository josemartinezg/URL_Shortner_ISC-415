<!-- Navigation -->
<nav class="navbar navbar-dark bg-dark static-top">
    <div class="container">
        <a class="navbar-brand" href="/"><img src="../assets/img/chindev3-1.gif" alt="animated"/></a>
        <#if usuario != "">
            <div class="dropdown">
                <button class="btn btn-outline-light dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <h4><strong>${usuario.username}</strong></h4>
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <a class="dropdown-item" href="/admin">Ver URLs</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item"  href="/hacerLogout">Log out</a>
                </div>
            </div>
        </#if>
        <#if usuario == "">
            <a class="btn btn-primary" href="/login">Log/Sign In</a>
        </#if>
    </div>
</nav>


<#--<!-- Navigation &ndash;&gt;-->
<#--<nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">-->
<#--    <div class="container">-->
<#--        <a class="navbar-brand" href="#">-->
<#--            <img src="http://placehold.it/150x50?text=Logo" alt="">-->
<#--        </a>-->
<#--        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">-->
<#--            <span class="navbar-toggler-icon"></span>-->
<#--        </button>-->
<#--        <div class="collapse navbar-collapse" id="navbarResponsive">-->
<#--            <ul class="navbar-nav ml-auto">-->
<#--                <li class="nav-item active">-->
<#--                    <a class="nav-link" href="#">Home-->
<#--                        <span class="sr-only">(current)</span>-->
<#--                    </a>-->
<#--                </li>-->
<#--                <li class="nav-item">-->
<#--                    <a class="nav-link" href="#">About</a>-->
<#--                </li>-->
<#--                <li class="nav-item">-->
<#--                    <a class="nav-link" href="#">Services</a>-->
<#--                </li>-->
<#--                <li class="nav-item">-->
<#--                    <a class="nav-link" href="#">Contact</a>-->
<#--                </li>-->
<#--            </ul>-->
<#--        </div>-->
<#--    </div>-->
<#--</nav>-->

<#--<!-- Page Content &ndash;&gt;-->
<#--<div class="container">-->
<#--    <h1 class="mt-4">Logo Nav by Start Bootstrap</h1>-->
<#--    <p>The logo in the navbar is now a default Bootstrap feature in Bootstrap 4! Make sure to set the width and height of the logo within the HTML or with CSS. For best results, use an SVG image as your logo.</p>-->
<#--</div>-->