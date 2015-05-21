/*
 * При полной загрузке документа
 * мы начинаем определять события
 */
$(document).ready(function () {
    var version = "v1";
    console.log("It is ready!!");

    $('#score').click(function () {
        console.log("Request: score!");
        $.ajax({
            url: "/api/v1/score",
            type: "get",
            success: function(result){
                console.log("Response on adminGetStatistic!");
                console.log($(result.code));
                console.log($(result));
            }
        });
    });

    $('#getStatistic').click(function () {
        console.log("Request: getStatistic!");
        $.ajax({
            url: "/api/v1/auth/admin?action=get",
            type: "post",
            success: function(result){
                console.log("Response on adminGetStatistic!");
                console.log($(result.code));
                console.log($(result));
            }
        });
    });

    $('#stopServer').click(function () {
        console.log("Request: stopServer!");
        $.ajax({
            url: "/api/v1/auth/admin?action=stop",
            type: "post",
            success: function(result){
                console.log("Response on adminStopServer!");
                console.log($(result.code));
                console.log($(result));
            }
        });
    });


    $('#signOut').click(function () {
        console.log("Request: signOut!");
        $.ajax({
            url: "/api/" + version + "/auth/signout",
            type: "POST",
            contentType: "application/json",
            success: function(result){
                console.log("Response on user_create!");
                console.log($(result.code));
                console.log($(result));
            }
        });
    });
});