<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="ph" scope="request" class="Bean.UploadBean"/>
<jsp:setProperty property="*" name="ph"/>

<%
    if (session.getAttribute("session") != "TRUE")
        response.sendRedirect("/SOC/SignIn");
%>
<html>
<head>
    <link rel="stylesheet" href="css/Upload.css">
</head>

<head>
    <title>SOC: Upload</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="homeTable">
    <h2 class="userTitle">Upload Photo: </h2>
    <div id="imageWrapper">


        <form id="uploadImages" action="${pageContext.request.contextPath}/Preview" method="post"
              enctype="multipart/form-data">

            <input id="uploadPhoto" type="file" onchange="checkExt() && preview()" name="file" size="50"/>
            <label for="uploadPhoto" id="uploadPhotoLabel" title="Choose file">
                <div id="uploadWrapper">

                    <div id="loading">
                        <img src='images/Loading.png'/>
                    </div>
                    <div id='photoWrapper'>
                        <img id='upPhoto' src='images/upload2.png'/>
                    </div>
                </div>
            </label>
        </form>
        <div id="uploadDescriptionWrapper">

            <div id="detailsWrapper" style="display: none">
                <!--
                    <div class="tabButs">
                        <button class="tablinks log active"  onclick="changeTab(event, 'details')">Details</button>
                        <button class="tablinks log "  onclick="changeTab(event, 'edit')">Edit (beta)</button>
                    </div>
            -->


                <div id="details" class="tabcontent active">
                    <form id="uploadDescription"
                          onsubmit="checkDetails() && confirm('Are you sure want to upload this?');"
                          action="${pageContext.request.contextPath}/Upload" method="post">
                        <input type="hidden" name="type" value="cloud"/>
                        <input id="FileInput" type="hidden" name="FileInput"/>
                        File: <span style="padding-bottom: 10px" id="imgFile"></span>

                        Name: <input class="uploadText" id="namejs" type="text" name="pname" size="40"
                                     placeholder="max 30 characters"/>

                        Description: <textarea class="uploadText" id="descriptionjs" name="description" cols="40"
                                               rows="5"
                                               placeholder="max 140 characters"></textarea>
                        <div>
                            Categories:
                            <button class="but" type="button" id="autoGen" style="width: 100px; top: 10px"
                                    onclick="autoOn()">Auto
                                Generate
                            </button>
                        </div>
                        <textarea class="uploadText" id="categoriesjs" name="categories" cols="71" rows="5"
                                  placeholder="Enter your own or auto generate (separated by spaces)"></textarea>

                        <span id="sliderLabel">Generate:</span>
                        <div id="sliderWrapper">
                            <div id="analyse">
                                <label for="amtRange" title="Amount of labels generated">Amount: </label>
                                <input type="range" min="1" max="20" value="5" name="amount" class="slider"
                                       id="amtRange" title="Amount of labels displayed">
                                <span id="amtDisplay" title="Amount of labels displayed"></span>
                                <button class="but" type="button" style="bottom:5px; margin-left: 10px"
                                        onclick="autoGenerate()">
                                    Analyse Photo
                                </button>
                            </div>
                            <script>
                                var sliderA = document.getElementById("amtRange");
                                var outputA = document.getElementById("amtDisplay");
                                outputA.innerHTML = sliderA.value;

                                sliderA.oninput = function () {
                                    outputA.innerHTML = this.value;
                                };

                                function autoGenerate() {
                                    var amt = document.getElementById("amtRange").value;
                                    var file = document.getElementById("imgFile").innerHTML;
                                    document.getElementById("categoriesjs").innerHTML = "Generating...";

                                    $.ajax({
                                        url: "Label",
                                        data: {
                                            amount: amt,
                                            fileName: file,
                                            type: 1
                                        },
                                        method: 'post',
                                        success: function (data) {
                                            document.getElementById("categoriesjs").innerHTML = data;
                                        },
                                        error: function (e) {
                                            alert('Error: ' + e.message);
                                        }
                                    });
                                }
                            </script>
                        </div>

                        <label for="pubjs">Public?</label>
                        <input id="pubjs" type="checkbox" name="pub" value="true"/>
                        <input class="but" style="top: 10px; width: 120px" type="submit" value="Upload File"/>
                    </form>
                </div>
                <!--
                    <div id="edit" class="tabcontent">

                        <span class="sliderLabelEdit">Blur:</span>
                        <div class="sliderWrapperEdit">


                            <button id="downBlur" onclick="">-</button>
                            <button id="upBlur" onclick="">+</button>

                        </div>

                        <span class="sliderLabelEdit">Brightness:</span>
                        <div class="sliderWrapperEdit">


                            <button id="downBright" onclick="downBright()">-</button>
                            <button id="upBright" onclick="upBright()">+</button>


                        </div>
                        <span class="sliderLabelEdit">Rotate:</span>
                        <div class="sliderWrapperEdit">
                            <button id="rotate" onclick="rotate()">Rotate</button>
                        </div>
                        <script>

                            var file  = document.getElementById("imgFile").innerHTML;
                            function rotate() {
                                editImage(90, file, 3);
                            }

                            function downBright(){
                                editImage(-1, file, 1);
                            }

                            function upBright(){
                                editImage(1, file, 1);
                            }


                            function editImage(out, f, edit) {
                               document.getElementById("loading").style.display = "block";
                                $.ajax({
                                    url: "edit",
                                    data: {output: out,
                                        fileName: f,
                                        type: edit},
                                    method: 'post',
                                    success: function(data)
                                    {
                                        $("#upPhoto").removeProp( "src" );
                                        $("#upPhoto").prop("src","upload/"+data+ '?' + (new Date().getTime()));
                                        document.getElementById("loading").style.display = "none";


                                    },
                                    error: function(e) {
                                        alert('Error: ' + e.message);
                                    }
                                });}

                            function removeElement(elementId) {
                                var element = document.getElementById(elementId);
                                element.parentNode.removeChild(element);
                            }

                            function addElement(parentId, elementTag, elementId, html) {
                                var p = document.getElementById(parentId);
                                var newElement = document.createElement(elementTag);
                                newElement.setAttribute('id', elementId);
                                newElement.innerHTML = html;
                                p.appendChild(newElement);
                            }
                        </script>
                    </div>
                    -->
            </div>
            <br>
            <button class="but" style=" float:right; margin-bottom: 10px; right:10px; width: 90px; margin-top: 5px"
                    onclick="window.location='user.jsp'">Back
            </button>
        </div>
    </div>
</div>
</body>
</html>