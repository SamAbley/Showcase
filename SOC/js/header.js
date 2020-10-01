function checkExt() {
    //check file extension
    var $fileUpload = document.getElementById("uploadPhoto");
    if (!(isImage($fileUpload.value))) {
        alert('Error: Please only upload the following image formats (jpg, gif, bmp, png)');
        return false;
    }
    return true;
}


function checkDetails() {
    //check name
    if (!(lessthan30(namejs.value))) {
        alert('Error: Please only enter less than 30 characters for the name');
        return false;
    }

    //check name
    if (!(lessthan140(descriptionjs.value))) {
        alert('Error: Please only enter less than 30 characters for the name');
        return false;
    }
    return true;
}

function lessthan30(name) {
    return name.toString().length < 30;
}

function lessthan140(description) {
    return description.toString().length < 140;
}

function getExtension(filename) {
    var parts = filename.split('.');
    return parts[parts.length - 1];
}

function isImage(filename) {
    var ext = getExtension(filename);
    switch (ext.toLowerCase()) {
        case 'jpg':
        case 'jpeg':
        case 'gif':
        case 'bmp':
        case 'png':
            return true;
    }
    return false;
}


function changeTab(evt, form) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks log");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(form).style.display = "grid";
    evt.currentTarget.className += " active";

}


function confirmDelete() {
    return confirm("Are you sure you want to delete this photo?");
}

function labelDetails() {
    var str = document.getElementById("str").val();
    var amt = document.getElementById("amt").val();
    confirm("Are you sure you want to find the first " + amt + " tags with strengths of " + str + " and above?");
}


function autoOn() {
    document.getElementById("sliderWrapper").style.display = "grid";
    document.getElementById("sliderLabel").style.display = "block";
    document.getElementById("autoGen").onclick = autoOff;
    document.getElementById("autoGen").className += " active";
}

function autoOff() {
    document.getElementById("sliderWrapper").style.display = "none";
    document.getElementById("sliderLabel").style.display = "none";
    document.getElementById("autoGen").onclick = autoOn;
    document.getElementById("autoGen").className = "but";

}

function addTo(list, feed) {
    feed = [];
    var split = list.split("\n");
    for (var i = 0; i <= split.length - 1; i++) {
        feed.push(split[i]);
    }
    return feed;
}

function stringBreaker(msg) {
    var result = [];
    var split = msg.indexOf(":");
    var m = msg.substring(split);

    var id = msg.substring(0, split);

    var reverse = reverseString(m);


    var date = reverse.substring(0, 14);
    reverse = reverseString(date);

    date = reverse.toString();

    var message = m.substring(2, m.length - 16);

    result[0] = id;
    result[1] = message;
    result[2] = date;
    return result;
}

function reverseString(str) {
    var newString = "";
    for (var i = str.length - 1; i >= 0; i--) {
        newString += str[i];
    }
    return newString;
}


function preview() {
    var form = $('#uploadImages')[0];
    document.getElementById("loading").style.display = "inline-block";
    // Create an FormData object
    var myFormData = new FormData(form);

    $.ajax({
        url: "Preview",
        enctype: 'multipart/form-data',
        type: "POST",
        processData: false, // important
        contentType: false, // important
        data: myFormData,
        cache: false,
        timeout: 600000,
        success: function (data) {

            var sPath = window.location.pathname;
            var sPage = sPath.substring(sPath.lastIndexOf('/') + 1);
            var type = data.substring(0, 1);
            if (type === "*") {
                alert("Upload Error: You cannot upload this image because it contains inappropriate content. (" + data.substring(1) + ")");
                window.location = sPage;
            } else if (type === "?") {
                alert("Upload Error: You cannot upload this image because it contains inappropriate text. (" + data.substring(1) + ")");
                window.location = sPage;
            } else {
                if (sPage == "messages.jsp") {
                    document.getElementById("upPhoto").setAttribute("src", "upload/" + data + '?' + (new Date().getTime()));
                    document.getElementById("imgFile").innerHTML = data;
                    document.getElementById("detailsWrapper").style.display = "inline-block";
                } else {
                    document.getElementById("upPhoto").setAttribute("src", "upload/" + data + '?' + (new Date().getTime()));
                    document.getElementById("imgFile").innerHTML = data;
                    document.getElementById("FileInput").value = data;
                    document.getElementById("detailsWrapper").style.display = "inline-block";
                }
            }
            document.getElementById("loading").style.display = "none";
        },
        error: function (e) {
            alert('Error: ' + e.message);
        }
    });
}

function upImage() {
    var file = document.getElementById("imgFile").innerHTML.replace(/\s/g, '');
    $.ajax({
        url: "Upload",
        data: {
            type: "cloud",
            file: file
        },
        method: 'post',
        success: function (data) {
        },
        error: function (e) {
            alert('Error: ' + e.message);
        }
    });
}


