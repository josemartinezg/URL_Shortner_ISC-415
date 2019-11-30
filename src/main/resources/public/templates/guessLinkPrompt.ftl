<a href="https://www.instagram.com/fria.js/?hl=es-la"></a>
<div id="qrcode"></div>
<script type="text/javascript">
    var qrcode = new QRCode("test", {
        text: "https://www.instagram.com/fria.js/?hl=es-la",
        <#--text: "/chinde.link/${link.urlGenerada}",-->
        width: 128,
        height: 128,
        colorDark : "#000000",
        colorLight : "#ffffff",
        correctLevel : QRCode.CorrectLevel.H
    });
</script>

<#if links?size != 0>
    <#list links as link>
        <a href="/chinde.link/${link.urlGenerada}"></a>
        <div id="qrcode"></div>
        <script type="text/javascript">
            var qrcode = new QRCode("test", {
                // text: "https://www.instagram.com/fria.js/?hl=es-la",
                text: "/chinde.link/${link.urlGenerada}",
                width: 128,
                height: 128,
                colorDark : "#000000",
                colorLight : "#ffffff",
                correctLevel : QRCode.CorrectLevel.H
            });
        </script>
    </#list>
</#if>