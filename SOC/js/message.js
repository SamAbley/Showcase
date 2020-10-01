var userName = null;
var userUrl = null;
var websocket = null;
var recipient = null;
var recipientName = null;
var recipientUrl = null;
var nots = 0;

function init(name, url) {
    if ("WebSocket" in window) {

        userName = name;
        userUrl = url;

        websocket = new WebSocket('ws://localhost/SOC/' + (userName + ""));
        websocket.onopen = function (data) {
        };

        websocket.onmessage = function (data) {
            setMessage(JSON.parse(data.data));
            return true;
        };

        websocket.onerror = function (e) {
            alert('An error occured, closing application');

            cleanUp();
            //window.location = '/SOC/user.jsp';
        };

        websocket.onclose = function (data) {
            cleanUp();

            var reason = (data.reason && true) ? data.reason : 'Goodbye';

            if (reason !== "Goodbye") {
                alert(reason);
                window.location = '/SOC/SignIn';
            }
        };
    } else {
        alert("Websockets not supported");
    }
}

function cleanUp() {
    userUrl = null;
    userName = null;
    websocket = null;
    recipient = null;
    recipientName = null;
    recipientUrl = null;
}

function sendMessage() {
    var msg = document.getElementById("message").value;
    var date = NOW();
    var messageContent = userName + "| " + msg + " |" + date;
    var message = buildMessage(userName, messageContent, "Text", recipient);
    document.getElementById("message").value = '';

    setMessage(message);
    websocket.send(JSON.stringify(message));
}

function sendImage() {
    var file = document.getElementById("imgFile").innerHTML.replace(/\s/g, '');
    $.ajax({
        url: "Upload",
        data: {
            type: "message",
            file: file
        },
        method: 'post',
        success: function (data) {
            var msg = data + "*" + document.getElementById("caption").value;
            var date = NOW();
            var messageContent = userName + "| " + msg + " |" + date;
            var message = buildMessage(userName, messageContent, "Img", recipient);

            setMessage(message);
            websocket.send(JSON.stringify(message));
            sendImgOff();
        },
        error: function (e) {
            alert('Error: ' + e.message);
        }
    });


}

function setNot() {
    nots++;
    checkNots();
}


function checkNots() {
    if ($('#mesNot').length)
        document.getElementById("mesNot").remove();

    var x = document.getElementsByClassName("notMes");
    var i;
    for (i = 0; i < x.length; i++) {
        x[i].remove();
    }
    if (nots > 0) {


        var child = '<div id="mesNot" class="notMes">' + nots + '</div>';

        var parent2 = document.getElementById("userName").innerHTML;
        document.getElementById("userName").innerHTML = child + parent2;

        if ($('#messageBut').length) {
            var parent = document.getElementById("messageBut").innerHTML;
            document.getElementById("messageBut").innerHTML = child + parent;
        }


    }
}

function buildMessage(userName, message, type, recipient) {
    return {
        username: userName,
        message: message,
        type: type,
        recipient: recipient
    };
}

function setMessage(msg) {
    var type = msg.type;
    var uName = msg.username;

    var split;

    if (type === "Text" || type === "Img") {

        var mes = msg.message;

        split = stringBreaker(mes);
        var user = split[0];
        var body = split[1];
        var date = split[2];
        var name;
        var url;

        if (user == recipient) {
            name = recipientName;
            url = recipientUrl;
        } else {
            name = "You";
            url = userUrl;
        }
        if (uName !== userName && uName !== recipient)
            setNot();


        if (uName !== userName && uName !== recipient) {

            if ($('#Not' + user).length) {
                var notEl = document.getElementById("Not" + user).innerText;
                var result = parseInt(notEl) + 1;
                document.getElementById("Not" + user).innerText = result;
            } else if ($('#User' + user).length) {
                var parent = document.getElementById("User" + user).innerHTML;
                var child = '<div id="Not' + user + '" class="notList">1</div>';
                document.getElementById("User" + user).innerHTML = child + parent;
            }
        } else {
            var currentHTML = document.getElementById('feed').innerHTML;
            var newElem;
            if (type === "Text") {
                newElem = '<div class="wholeMessage"><div class="date">' + date
                    + '</div>'
                    + '<div class="userPic">'
                    + '<img src="' + url + '">'
                    + '</div>'
                    + '<div class="messageName">'
                    + name
                    + '</div>'
                    + '<div class="messageBody">'
                    + body
                    + '</div>'
                    + '</div>';
                document.getElementById('feed').innerHTML = newElem + currentHTML;

            } else if (type === "Img") {
                //split message into img and caption
                var ind = body.indexOf("*");

                var caption = body.substring(ind + 1);
                var img = body.substring(0, ind);

                newElem = '<div class="wholeMessage"><div class="date">' + date
                    + '</div>'
                    + '<div class="userPic">'
                    + '<img src="' + url + '">'
                    + '</div>'
                    + '<div class="messageName">'
                    + name
                    + '</div>'
                    + '<div class="messageBody">'
                    + '<img class="messageImg" src="' + img + '">'
                    + '<p>' + caption + '</p>'
                    + '</div>'
                    + '</div>';
                document.getElementById('feed').innerHTML = newElem + currentHTML;
            }
        }
    } else if (type === "Not") {


        split = msg.message.split(" ");
        nots = split.length - 1;
        checkNots();
        for (var i = 0; i < split.length - 1; i++) {
            var not = split[i];
            if (split.length > 1)
                if ($('#Not' + not).length) {
                    var notEl = document.getElementById("Not" + not).innerText;
                    var result = parseInt(notEl) + 1;
                    document.getElementById("Not" + not).innerText = result;
                } else if ($('#User' + not).length) {
                    var parent = document.getElementById("User" + not).innerHTML;
                    var child = '<div id="Not' + not + '" class="notList">1</div>';
                    document.getElementById("User" + not).innerHTML = child + parent;
                }
        }
    } else if (type === "On") {
        //User in list
        var parent = document.getElementById("User" + uName).innerHTML;
        var child = '<div id="Online' + uName + '" class="notOnline"></div>';
        document.getElementById("User" + uName).innerHTML = child + parent;

        //Chat Title
        if (recipient === uName) {
            var parent2 = document.getElementById("feedTitle").innerHTML;
            var child2 = '<div id="NameOnline' + uName + '" class="nameOnline"></div>';
            document.getElementById("feedTitle").innerHTML = child2 + parent2;
        }
    } else if (type === "Off") {

        //User in list
        if ($('#Online' + uName).length)
            document.getElementById("Online" + uName).remove();

        //Chat title
        if (recipient === uName) {
            document.getElementById("NameOnline" + uName).remove();
        }
    }


}


function stringBreaker(msg) {
    var result = [];
    var split = msg.indexOf("|");
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

function selectUser(uid, name, url, on) {
    recipient = uid;
    recipientName = name;
    recipientUrl = url;
    document.getElementById("feed").innerHTML = "";
    document.getElementById("picL").src = url;
    document.getElementById("feedTitle").innerText = name;
    document.getElementById("feedCover").style.display = "none";


    if (on === 1) {
        var parent2 = document.getElementById("feedTitle").innerHTML;
        var child2 = '<div id="NameOnline' + name + '" class="nameOnline"></div>';
        document.getElementById("feedTitle").innerHTML = child2 + parent2;
    }
    //send feed request
    var messageContent = " ";
    var type = "Feed";
    var message = buildMessage(userName, messageContent, type, recipient);
    websocket.send(JSON.stringify(message));
    var fd = document.getElementById("feed");
    var scroll = scrollAtBottom(fd);

    if (scroll) {
        updateScroll(fd);
    }
    var x = document.getElementsByClassName("active");
    var i;
    for (i = 0; i < x.length; i++) {
        x[i].className = "but user";
    }
    document.getElementById("User" + uid).className += " active";

    if ($('#Not' + uid).length) {
        var rem = document.getElementById("Not" + uid).innerText;
        nots = nots - parseInt(rem);
        checkNots();


        document.getElementById("Not" + uid).remove();
    }


}


function scrollAtBottom(el) {
    return (el.scrollTop + 5 >= (el.scrollHeight - el.offsetHeight));
}

function updateScroll(el) {
    el.scrollTop = el.scrollHeight;
}

/**
 * @return {string}
 */
function NOW() {

    var date = new Date();
    var yy = date.getFullYear().toString().substr(2, 2);
    var dd = date.getDate();
    var mm = (date.getMonth() + 1);

    if (dd < 10)
        dd = "0" + dd;

    if (mm < 10)
        mm = "0" + mm;

    var cur_day = dd + "/" + mm + "/" + yy;

    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();

    if (hours < 10)
        hours = "0" + hours;

    if (minutes < 10)
        minutes = "0" + minutes;

    if (seconds < 10)
        seconds = "0" + seconds;

    return hours + ":" + minutes + " " + cur_day;

}

function searchUsers() {
    var user = document.getElementById("listSearchInput").value;

    $.ajax({
        url: "Search",
        data: {query: user},
        method: 'get',
        success: function (data) {
            $("#list").load("messages.jsp #searchWrapper");
        },
        error: function (e) {
            alert('Error: ' + e.message);
        }
    });

}

function resetUsers() {
    var name = userName;
    var url = userUrl;
    setTimeout(function () {
        init(name, url);
    }, 100);
    $("#list").load("messages.jsp #searchWrapper");
    websocket.close();
}

function startWebSoc(id, url) {
    userName = id;
    userUrl = url;
    var name = userName;
    var u = userUrl;
    setTimeout(function () {
        init(name, u);
    }, 100);

    websocket.close();
}

function sendImgOn() {
    document.getElementById("imgCover").style.display = "block";
}

function sendImgOff() {
    document.getElementById("caption").value = '';
    document.getElementById("imgCover").style.display = "none";
    document.getElementById("detailsWrapper").style.display = "none";
    document.getElementById("imgFile").innerHTML = "Please Choose a file";
    document.getElementById("upPhoto").setAttribute("src", "images/upload2.png");
}

