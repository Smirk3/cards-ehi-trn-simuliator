function withAction(element, action){
    element.closest("form").attr("action", action);
    return element;
}

function submitForm(element){
    element.closest("form").submit;
}

function editRequest(){
    $("#requestView").hide();
    $("#actionButtons").hide();
    $("#requestEdit").show();
}

function saveRequest(){
    var a = $("#xmlRequestEdit").val();
    $("#xmlRequestView").val('asd');

    $("#requestView").show();
    $("#actionButtons").show();
    $("#requestEdit").hide();
}