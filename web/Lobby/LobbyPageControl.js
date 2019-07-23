var refreshRate = 2000; //milli seconds
//var LOGGED_USERS_URL = buildUrlWithContextPath("LoggedUsersStats");
var SelectedRoom;
window.onload = function ()
{

    refreshLoginStatus();
    refreshUserList();
    refreshGamerooms();
    refreshXMLStatus();
    setInterval(refreshXMLStatus,refreshRate);
    setInterval(refreshUserList, refreshRate);
    setInterval(refreshGamerooms,refreshRate);
    setInterval(refreshLoginStatus,refreshRate)

};

function refreshXMLStatus(){
    $.ajax
    ({
        url: 'UpLoadXmlFile',
        data: {
            action: "getErrorStatus"
        },
        type: 'GET',
        success: refreshErrorStatusCallback
    });
}
function refreshErrorStatusCallback(error)
{
    $('.errorXml').text(error);
}

function refreshLoginStatus() {
    $.ajax
    ({
        url: 'Lobby',
        data: {
            action: "getLoggedUsername"
        },
        type: 'GET',
        success: refreshLoginStatusCallback
    });
}


function refreshLoginStatusCallback(username)
{
    $('.userNameSpan').text("Hello " + username + " Welcome to Conquers!");
}
//refresh connected users list
function refreshUserList() {
    $.ajax(
        {
            url:  'Lobby',
            data: {
                action: "getLoggedUsers"
            },
            type: 'GET',
            success: refreshUserListCallback
        }
    );
}



function refreshUserListCallback(players) {
    //clear all current users
    $("#userslist").empty();
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(players || [], function (index, username) {
        username = players[index];
        $("#userslist").append("<h4>" + username + "</h4>").append("<br>");
    });
}

    function refreshGamerooms() {
        $.ajax
        ({
            url: 'UpLoadXmlFile',
            data: {
                action: "getRoomslist"
            },
            type: 'POST',
            success: refreshroomslistCallback
        }
        );
    }
    function refreshroomslistCallback(rooms)
    {

        $("#roomsarea").empty();

        $.each(rooms || [], function(index, GameName) {
            GameName=rooms[index];
            var btn = document.createElement("BUTTON");
            btn.innerHTML = GameName;
            btn.onclick=opendialog;
            $("#roomsarea").append(btn).append("<br>");
        });

    }
function opendialog(event) {
    var button = event.currentTarget;
     SelectedRoom = button.innerHTML.toString();
    $.ajax
    (
        {
            url: 'UpLoadXmlFile',
            data: {
                action: 'gameDetails',
                key: SelectedRoom
            },
            type: 'GET',
            success: createGameDialogCallback
        }
    )
}

function createGameDialogCallback(json) {
        var div = $('.dialogDiv')[0];
        div.style.display = "block";

        var creatorName = json.roomCreator;
        var boardSize = json.Rows + " X " + json.Columns;
        var playerNumber = json.numberOfOnlinePlayers + " / " + json.maxNumberOfOnlineUsers;
        var territories=json.engine.descriptor.territoryMap;
        var rows=json.Rows;
        var columns=json.Columns;
        var title=json.engine.descriptor.gameTitle
        $('.gametitle').text("Game title: " + title + ".");
        $('.creatorName').text("Game Creator: " + creatorName + ".");
        $('.boardSize').text("Board size: " + boardSize);
        $('.playerNumber').text("Players : " + playerNumber);
        if (json.numberOfOnlinePlayers==json.maxNumberOfOnlineUsers) {
            var status = "The game is active";
        }
        else
            var status="Game not active";
        $('.status').text(status);
        $('.detailsOfBoard').text("Territories details: ");
        for(i=0;i<rows*columns;i++)
        {
            var j=i+1;
            var threshold="threshold: "+territories[j].Threshold;
            var id="Terrotory id: " +j;
            var profit="profit: "+territories[j].profit;
            $('.detailsOfBoard').append("<h4>" + id + "</h4>");
            $('.detailsOfBoard').append("<h5>" + threshold + "</h5>");
            $('.detailsOfBoard').append("<h5>" + profit + "</h5>");
        }



        $('.detailsOfUnits').append("<h2> UNITS DETAIL: </h2>");
        var unitsmap=json.engine.descriptor.unitMap;
        if(unitsmap.Soldier!=null) {
            var unit = unitsmap.Soldier;
            var type = "Type: " + unit.type;
            var maxfp = "Max FP: " + unit.maxFP;
            var rank = "Rank: " + unit.rank;
            var purches = "purches: " + unit.purchase;
            var competence = "reduction: " + unit.competenceReduction;
            $('.detailsOfUnits').append("<h5>" + type + "</h5>");
            $('.detailsOfUnits').append("<h5>" + rank + "</h5>");
            $('.detailsOfUnits').append("<h5>" + maxfp + "</h5>");
            $('.detailsOfUnits').append("<h5>" + purches + "</h5>");
            $('.detailsOfUnits').append("<h5>" + competence + "</h5>");
        }
        if(unitsmap.Tank!=null) {
            var unit = unitsmap.Tank;
            var type = "Type: " + unit.type;
            var maxfp = "Max FP: " + unit.maxFP;
            var rank = "Rank: " + unit.rank;
            var purches = "purches: " + unit.purchase;
            var competence = "reduction: " + unit.competenceReduction;
            $('.detailsOfUnits').append("<h5>" + type + "</h5>");
            $('.detailsOfUnits').append("<h5>" + rank + "</h5>");
            $('.detailsOfUnits').append("<h5>" + maxfp + "</h5>");
            $('.detailsOfUnits').append("<h5>" + purches + "</h5>");
            $('.detailsOfUnits').append("<h5>" + competence + "</h5>");
        }
        if(unitsmap.Missile!=null) {
            var unit = unitsmap.Missile;
            var type = "Type: " + unit.type;
            var maxfp = "Max FP: " + unit.maxFP;
            var rank = "Rank: " + unit.rank;
            var purches = "purches: " + unit.purchase;
            var competence = "reduction: " + unit.competenceReduction;
            $('.detailsOfUnits').append("<h5>" + type + "</h5>");
            $('.detailsOfUnits').append("<h5>" + rank + "</h5>");
            $('.detailsOfUnits').append("<h5>" + maxfp + "</h5>");
            $('.detailsOfUnits').append("<h5>" + purches + "</h5>");
            $('.detailsOfUnits').append("<h5>" + competence + "</h5>");
        }


}

function hideRoom()
{
    var div = $('.dialogDiv')[0];
    div.style.display = "none";
}


function addUser()
{


    $.ajax
    ({
        url: 'UpLoadXmlFile',
        data: {
            action: "addUserToRoom",
            selectedroom:SelectedRoom
        },
        type: 'GET',
        success: window.location = "../GameRoom/GameRoom.html"

    });
}



