$(document).ready(function () {

    var environments = [];

    $('#form-autocomplete').mdb_autocomplete({
        data: environments
    });

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/ehi/env/getEnvironments",
            //data: JSON.stringify(loginForm),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {

                var json = "<h4>Ajax Response</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                //$('#feedback').html(json);

                console.log("SUCCESS : ", data);
                $("#btn-login").prop("disabled", false);

            },
            error: function (e) {

                var json = "<h4>Ajax Response Error</h4><pre>"
                    + e.responseText + "</pre>";
                //$('#feedback').html(json);

                console.log("ERROR : ", e);
                $("#btn-login").prop("disabled", false);

            }
        });
});