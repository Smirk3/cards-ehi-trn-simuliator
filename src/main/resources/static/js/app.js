function withAction(element, action){
    element.closest("form").attr("action", action);
    return element;
}

function submitForm(element){
    element.closest("form").submit;
}