
window.onload = function()
{
    checkLoginStatus();
    setInterval(checkLoginStatus, 2000);
};

function checkLoginStatus() {
    $.ajax
    ({
        url: 'login',
        data: {
            action: "status"
        },
        type: 'GET',
        success: statusCallback
    });
}

function statusCallback(json)
{
    if (json.isConnected && json.gameNumber != -1)
    {
        window.location = "GameRoom.html";
    }
    else if (json.isConnected)
    {
        window.location = "Lobby.html";
    }
}

function loginClick()
{
    event.preventDefault();

    var userName = $('.UserNameInput').val();
    $.ajax
    ({
        url: 'login',
        data:
        {
            action: "login",
            userName: userName,
        },
        type: 'GET',
        success: loginCallback
    });
}

function loginCallback(json)
{
    if (json.isConnected)
    {
        window.location = "Lobby.html";
    }
    else
    {
        alert(json.errorMessage);
    }
}