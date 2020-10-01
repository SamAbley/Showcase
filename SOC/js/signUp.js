function confirmPass() {
    var criteria = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
    if (pass.value !== pass2.value) {
        alert("Error: Your passwords do not match, please try again");
        return false;
    }else if(!pass.value.match(criteria)){
        alert("Error: Your password needs to be between 6-20 characters long containing at least one numeric digit, one uppercase and one lowercase letter , please try again");
        return false;
    }

    return true;
}