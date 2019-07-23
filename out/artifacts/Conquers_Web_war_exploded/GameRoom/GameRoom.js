var cols;
var rows;
var userName;
var territoriesMap;
var alertGameStart=0;
var targetTerritory
var enginestart=false;
 window.onload=function ()
 {
     setInterval(crateGameDetails,2000);
 };

function crateGameDetails() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "getGameDetails"
        },
        type: 'GET',
        success: setGameDetails

    });
}

function setGameDetails(data) {
    cols = data.cols;
    rows = data.rows;
    userName = data.userName;
    $('.userNameSpan').text("hi "+userName);
    $('.gameStatus').text("the game start");
    territoriesMap=data.gameEngine.descriptor.territoryMap;
    if (data.started==true)
    {
        if (enginestart==false)
        {
            $.ajax
            ({
                url: 'SingleGame',
                data: {
                    action: "startGame"

                },
                type: 'GET',

            });
            success:enginestart=true;
        }
        alertGameStart++
        if (alertGameStart == 1){
            alert("The game started");
        }

        drawBoard();
        drawUnitTable();
    }
}

function drawBoard()
{
        // get the reference for the body
        var div1 = document.getElementById('boardBody');
        $("#boardBody").empty();
        var i=1;
        // creates a <table> element
        var tbl = document.createElement("table");
        // creating rows
        for (var r = 1; r <= rows; r++) {
            var row = document.createElement("tr");

            // create cells in row
            for (var c = 1; c <= cols; c++) {
                var cell = document.createElement("td");
                var id="id "+territoriesMap[i].id+"\n";
                var threshold="threshold "+territoriesMap[i].Threshold+"\n";
                var profit="profit "+territoriesMap[i].profit;
                var cellText = document.createTextNode(id  + threshold  + profit);
                i++;
                cell.appendChild(cellText);
                cell.onclick=showDetail;
                row.appendChild(cell);
            }

            tbl.appendChild(row); // add the row to the end of the table body
        }

        div1.appendChild(tbl); // appends <table> into <div1>

}
function drawUnitTable()
{

}


function showDetail(event)
{
var target=event.currentTarget;
var string=target.innerHTML.toString();
if (string[4]=="\n")
{
    var id=string[3];

}
else
{
    var id=string[3].toString()+string[4].toString();
}

targetTerritory=id;
    $('.TerritoryID').text("Territory Id: "+targetTerritory);
//עדכון הצבא ברשותו
}
