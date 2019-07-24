var cols;
var rows;
var userName;
var territoriesMap;
var alertGameStart=0;
var targetTerritory;
var engineStart=false;
var buttonPressed;
var dataTableSize;
 window.onload=function ()
 {
     createGameDetails();
 };
function onLeaveGameClick() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "LeaveGame"
        },
        type: 'GET',
        success: window.location = "../Lobby/lobby.html"

    });


}
function createGameDetails() {
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
function turnOffButtons() {

    document.getElementById('maintenance').style.visibility='hidden';
    document.getElementById('calculatedRisk').style.visibility='hidden';
    document.getElementById('wellOrchestrated').style.visibility='hidden';
    document.getElementById('naturalTerritory').style.visibility='hidden';
    document.getElementById('addArmy').style.visibility='hidden';
    document.getElementById('tableData').style.visibility='hidden';
    document.getElementById('confirm').style.visibility='hidden';
    //document.getElementById('togglee').style.visibility = 'visible';



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
        if (engineStart==false)
        {
            $.ajax
            ({
                url: 'SingleGame',
                data: {
                    action: "startGame"

                },
                type: 'GET',

            });
            success:engineStart=true;
        }
        alertGameStart++
        if (alertGameStart == 1){
            alert("The game started");
        }
        document.getElementById('leaveButton').style.visibility='hidden';

        drawBoard();
        drawUnitTable();
    }
}
function drawUnitTable()
{
    $.ajax
({
    url: 'SingleGame',
    data: {
        action: "dataTableDetails"
    },
    type: 'GET',
    success: drawDataTable

});
}
function drawDataTable(dataTable) {
    $("#dataTable").empty();
    dataTableSize=dataTable.length;
    console.log(dataTable);
      for ( var i = 0; i < dataTableSize; i++) {
          var tr = document.createElement("tr");
          var td = document.createElement("td");
          td.appendChild(document.createTextNode(dataTable[i].rank));
          tr.appendChild(td);
          td=document.createElement("td");
          var type= document.createTextNode(dataTable[i].type)
          type.setAttribute("id","unitType"+i);
          td.appendChild(type);
          tr.appendChild(td);
          td=document.createElement("td");
          td.appendChild(document.createTextNode(dataTable[i].fp));
          tr.appendChild(td);
          td=document.createElement("td");
          var amountInput = document.createElement("INPUT");
          amountInput.setAttribute("size",2);
          amountInput.setAttribute("id","unitDataInput"+i);
          td.appendChild(amountInput);
          tr.appendChild(td);
          td=document.createElement("td");
          td.appendChild(document.createTextNode(dataTable[i].price));
          tr.appendChild(td);
          td=document.createElement("td");
          td.appendChild(document.createTextNode(dataTable[i].subduction));
          tr.appendChild(td);
          td=document.createElement("td");
          tr.appendChild(td);

          document.getElementById("dataTable").appendChild(tr);
      }

}
function enterAmount() {


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

function showTerritoryId(event)
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
}
function showTerritoryInfo(event) {


}
function showDetail(event)
{
showTerritoryId(event);
showTerritoryInfo()
//עדכון הצבא ברשותו
}


function onMaintenanceClick(){
    buttonPressed=1;
}
function onCalculatedRiskClick(){
    buttonPressed=2;

}
function onWellOrchestratedClick(){
    buttonPressed=3;
}
function onNaturalTerritoryClick(){
    buttonPressed=4;
}
function onAddArmyClick(){
    buttonPressed=5;
}
function onRetireClick(){
    buttonPressed=6;
}
function onConfirmClick(){
    var params = {};
    if(buttonPressed === 1)
    {
        $.ajax
        ({
            url: 'SingleGame',
            data: {
                action: "maintenance",
                buttonPressed: buttonPressed,
                targetTerritory:targetTerritory
            },
            type: 'GET',
            success: checkEnoughTourings

        });
    }
    if(buttonPressed === 2){
        for ( var i = 0; i < dataTableSize; i++) {
            params["PreattackerArmy"] ={i:[document.getElementById("unitType" + i),document.getElementById("unitDataInput" + i)]};
            params["action"]="calculatedRisk";
            params["buttonPressed"]=buttonPressed;
            params["targetTerritory"]=targetTerritory;
        }
        $.ajax
        ({
            url: 'SingleGame',
            data:params,
            type: 'GET',
            success: resultCalculatedRisk

        });
    }
    if(buttonPressed === 3)
    {
        for ( var i = 0; i < dataTableSize; i++) {
            params["PreattackerArmy"] ={i:[document.getElementById("unitType" + i),document.getElementById("unitDataInput" + i)]};
            params["action"]="wellOrchestrated";
            params["buttonPressed"]=buttonPressed;
            params["targetTerritory"]=targetTerritory;
        }
        $.ajax
        ({
            url: 'SingleGame',
            data:params,
            type: 'GET',
            success: resultWellOrchestrated
        });
    }
    if(buttonPressed === 4)
    {
        for ( var i = 0; i < dataTableSize; i++) {
            params["PreattackerArmy"] ={i:[document.getElementById("unitType" + i),document.getElementById("unitDataInput" + i)]};
            params["action"]="naturalTerritory";
            params["buttonPressed"]=buttonPressed;
            params["targetTerritory"]=targetTerritory;
        }
        $.ajax
        ({
            url: 'SingleGame',
            data:params,
            type: 'GET',
            success: checkEnoughTouringsforNaturalTerritory
        });
    }
    if(buttonPressed === 5)
    {

        for ( var i = 0; i < dataTableSize; i++) {
            params["PreattackerArmy"] ={i:[document.getElementById("unitType" + i),document.getElementById("unitDataInput" + i)]};
            params["action"]="addArmy";
            params["buttonPressed"]=buttonPressed;
            params["targetTerritory"]=targetTerritory;
        }
        $.ajax
        ({
            url: 'SingleGame',
            data:params,
            type: 'GET',
            success: checkEnoughTourings
        });
    }
    if(buttonPressed === 6)
    {
        $.ajax
        ({
            url: 'SingleGame',
            data: {
                action: "retire",
                buttonPressed: buttonPressed
            },
            type: 'GET',
            success: deletePlayer

        });
    }
    //updatePlaying Player(next turn)
    drawBoard();

//apicall
}
function onEndTurnClick(){
    //apicall

}
function resultCalculatedRisk() {

}
function checkEnoughTouringsforNaturalTerritory() {

}
function checkEnoughTourings() {

}
function resultWellOrchestrated() {

}
function deletePlayer() {

}
