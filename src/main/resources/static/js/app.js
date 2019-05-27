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

String.prototype.htmlEscape = function() {
    return $('<div/>').text(this.toString()).html();
};

function saveRequest(){
    var xml = $("#xmlRequestEdit").val();
    $("#xmlRequestView").html(xml.htmlEscape());
    $("#xmlRequest").val(xml);

    $("#requestView").show();
    $("#actionButtons").show();
    $("#requestEdit").hide();

    Prism.highlightElement($("#xmlRequestView")[0]);
}

function toggleVisibility(elementId){
    $('#'+elementId).toggle();
}