
$(document).ready(function() {
   showAuth();

});

function authenticate() {
    var login = {}
    login["user"] = $("#user").val();
    login["password"] = $("#password").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/ehi/wiki/bas/login",
        data: JSON.stringify(login),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data.status == "OK"){
                $('#alertError').html('').hide();
                window.location.href = "/ehi/env/show/edit";
            } else {
                $('#alertInfo').hide();
                $('#alertError').html(data.message).show();
            }
        },
        error: function (e) {
            console.log("ERROR authenticate: ", e);
        }
    });
}

function skipAuthentication() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/ehi/wiki/bas/login/skip",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            hideAuthPopup();
        },
        error: function (e) {
            hideAuthPopup();
            console.log("ERROR: skipAuthentication", e);
        }
    });
}

function showAuth() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/ehi/wiki/bas/login/show",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data.show == true) {
                showAuthPopup();
            }
        },
        error: function (e) {
            console.log("ERROR: showAuth", e);
        }
    });
}

function showAuthPopup() {
    $('#envEditForm').hide();
    $('#overlay').fadeIn(300);
}

function hideAuthPopup() {
    $('#overlay').fadeOut(300);
    $('#envEditForm').show();
}

function resolveCustomOptionSelected(select) {
    $('#envsListDiv').show();
    $('#envsList').attr('disabled', 'disabled');
    $('#envCustomDiv').show();
    $('#envCustom').show();
}