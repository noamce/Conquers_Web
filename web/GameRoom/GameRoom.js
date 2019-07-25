var cols;
var rows;
var userName;
var territoriesMap;
var alertGameStart=0;
var targetTerritory;
var engineStart=false;
var buttonPressed;
var dataTableSize;
var territoryDataTableSize;
var showTerritoryInfoFlag=false;

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

function getCurrentPlayerInfo() {
    if(engineStart){
        $.ajax
        ({
            url: '/playerInfo',
            type: 'GET',
            success: processInfo

        });
    }
}


function processInfo(data){
    console.log(data);

    var element = document.getElementById("nameOfcurrentPlayer");
    element.innerHTML="Player Turn:"+data.name.toString();
    var element = document.getElementById("roundNumberinfo");
    element.innerHTML=data.roundNumber;
    element = document.getElementById("totalCycleinfo");
    element.innerHTML=" / " +data.totalCycles.toString();
    findPlayerColor(data.color);
    element = document.getElementById("TurringsInfo");
    element.innerHTML="Turrings: "+ data.funds;
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
function findPlayerColor(color){

    if (color===1) {
        $('.PlayerColor').text("Player color: RED");
        //document.getElementById("PlayerColor").style.color= "red";
        //player_color.setTextFill(Color.RED);
    }
    if (color===2) {
        $('.PlayerColor').text("Player color: BLUE");
       // document.getElementById("PlayerColor").style.color= "blue";
       // player_color.setTextFill(Color.BLUE);
    }
    if (color===3) {
        $('.PlayerColor').text("Player color: GREEN");
        //document.getElementById("PlayerColor").style.color= "green";
        //player_color.setTextFill(Color.GREEN);
    }
    if (color===4) {
        $('.PlayerColor').text("Player color: YELLOW");
       // document.getElementById("PlayerColor").style.color= "yellow";
        //player_color.setTextFill(Color.YELLOW);
    }

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
    if (data.started===true)
    {
        if (engineStart===false)
        {
            $.ajax
            ({
                url: 'SingleGame',
                data: {
                    action: "startGame"

                },
                type: 'GET',

            });
            success: engineStart=true;
        }
        alertGameStart++
        if (alertGameStart === 1){
            alert("The game started");
        }
        document.getElementById('leaveButton').style.visibility='hidden';
        getCurrentPlayerInfo();
        drawBoard();
        drawUnitTable();

    }
}
function gameHasStarted() {
    engineStart=true;
    getCurrentPlayerInfo();

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
          td.appendChild(type);
          tr.appendChild(td);
          td=document.createElement("td");
          td.appendChild(document.createTextNode(dataTable[i].fp));
          tr.appendChild(td);
          td=document.createElement("td");
          var amountInput = document.createElement("INPUT");
          amountInput.setAttribute("size",2);
          amountInput.setAttribute("id",dataTable[i].type);
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
function showTerritoryInfo() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "territoryDataTable",
            showFlag: showTerritoryInfoFlag,
            territoryId:targetTerritory
        },
        type: 'GET',
        success: drawTerritoryDataTable

    });

}
function drawTerritoryDataTable(territoryDataTableInfo) {
    //drawTerritory army table including fp and maintenace
    $("#territoryTableDataBody").empty();
    territoryDataTableSize=territoryDataTableInfo.length;
    console.log(territoryDataTableInfo);
    for ( var i = 0; i < territoryDataTableSize; i++) {
        var tr = document.createElement("tr");
        var td = document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].rank));
        tr.appendChild(td);
        td=document.createElement("td");
        var type= document.createTextNode(territoryDataTableInfo[i].type)
        td.appendChild(type);
        tr.appendChild(td);
        td=document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].fp));
        tr.appendChild(td);
        td=document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].amount));
        tr.appendChild(td);
        td=document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].maintenance));
        tr.appendChild(td);


        document.getElementById("territoryTableDataBody").appendChild(tr);
    }
    $('.TotalFP').text("Total Fire Power: " + territoryDataTableInfo[0].totalFirePower);
    $('.TotalMaintenanceCost').text("Total Cost of Maintenance: " + territoryDataTableInfo[0].maintenanceCost);

}
function whatVisible(territoryButtonInfo){
    territoryButtonInfo=false;
    var round=document.getElementById("roundNumberinfo");
    if(round === 1){
        document.getElementById("retire").style.visibility="hidden";
        document.getElementById("endTurn").style.visibility="hidden";
        //check if this is not a conquerd territory
        if(territoryButtonInfo.territoryConquered)
        {
            turnOffButtons();
        }
        else
        {
            document.getElementById("naturalTerritory").style.visibility="visible";
            document.getElementById("wellOrchestrated").style.visibility="hidden";
            document.getElementById("calculatedRisk").style.visibility="hidden";
            document.getElementById("maintenance").style.visibility="hidden";
            document.getElementById("addArmy").style.visibility="hidden";
        }
    }
    else{
        //if the game is over or there is only one player
        if(territoryButtonInfo.onlyOnePlayer || territoryButtonInfo.gameOver){
            //turnoffButtons include retire and endturn
            document.getElementById("retire").style.visibility="hidden";
            document.getElementById("endTurn").style.visibility="hidden";
            turnOffButtons();
        }
        else{
            // retire and endturn on
            document.getElementById("retire").style.visibility="visible";
            document.getElementById("endTurn").style.visibility="visible";
            //if this territory is conquerd
            if(territoryButtonInfo.territoryConquered){
                //if is this  territory is belong to currrent player
                if(territoryButtonInfo.isTerritoryBelongsCurrentPlayer){
                    // territory actions on
                    document.getElementById("maintenance").style.visibility="visible";
                    document.getElementById("addArmy").style.visibility="visible";
                    document.getElementById("naturalTerritory").style.visibility="hidden";
                    document.getElementById("wellOrchestrated").style.visibility="hidden";
                    document.getElementById("calculatedRisk").style.visibility="hidden";
                    //drawTerritory army table including fp and maintenace
                    territoryButtonInfo=true;
                }
                else
                {
                    //clear the maintence and total firepower of this territory
                    //if this territoryvaild or the currentplayer dont have territories
                    if(territoryButtonInfo.isTargetTerritoryValid || territoryButtonInfo.playerDontHaveTerritories)
                    {
                        // buttons of battles on
                        document.getElementById("naturalTerritory").style.visibility="hidden";
                        document.getElementById("wellOrchestrated").style.visibility="visible";
                        document.getElementById("calculatedRisk").style.visibility="visible";
                        document.getElementById("maintenance").style.visibility="hidden";
                        document.getElementById("addArmy").style.visibility="hidden";
                    }
                    else
                    {
                        turnOffButtons();
                    }
                }
            }
            else{
                if(territoryButtonInfo.playerDontHaveTerritories){
                    //naturalterritory on
                    document.getElementById("naturalTerritory").style.visibility="visible";
                    document.getElementById("wellOrchestrated").style.visibility="hidden";
                    document.getElementById("calculatedRisk").style.visibility="hidden";
                    document.getElementById("maintenance").style.visibility="hidden";
                    document.getElementById("addArmy").style.visibility="hidden";
                }
                else{
                    if(territoryButtonInfo.isTargetTerritoryValid){
                        //naturalterritory on
                        document.getElementById("naturalTerritory").style.visibility="visible";
                        document.getElementById("wellOrchestrated").style.visibility="hidden";
                        document.getElementById("calculatedRisk").style.visibility="hidden";
                        document.getElementById("maintenance").style.visibility="hidden";
                        document.getElementById("addArmy").style.visibility="hidden";
                    }
                    else{
                        turnOffButtons();
                    }
                }


            }
        }
    }

}
function territoryClicked() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "territoryClicked",
            targetTerritory:targetTerritory
        },
        type: 'GET',
        success: whatVisible

    });
}

function showDetail(event)
{
    showTerritoryId(event);
    territoryClicked();
    showTerritoryInfo();
//עדכון הצבא ברשותו
}
function notClickedButtons() {
    (document.getElementById("maintenance")).style.border = "1px solid black";
    (document.getElementById("calculatedRisk")).style.border = "1px solid black";
    (document.getElementById("wellOrchestrated")).style.border = "1px solid black";
    (document.getElementById("naturalTerritory")).style.border = "1px solid black";
    (document.getElementById("addArmy")).style.border = "1px solid black";
    (document.getElementById("retire")).style.border = "1px solid black";
    (document.getElementById("confirm")).style.border = "1px solid black";
    (document.getElementById("endTurn")).style.border = "1px solid black";

}
function onMaintenanceClick(){
    notClickedButtons();
    buttonPressed=1;
    (document.getElementById("maintenance")).style.border = "3px solid blue";
}
function onCalculatedRiskClick(){
    notClickedButtons();
    buttonPressed=2;
    (document.getElementById("calculatedRisk")).style.border = "3px solid blue";
}
function onWellOrchestratedClick(){
    notClickedButtons();
    buttonPressed=3;
    (document.getElementById("wellOrchestrated")).style.border = "3px solid blue";
}
function onNaturalTerritoryClick(){
    notClickedButtons();
     (document.getElementById("naturalTerritory")).style.border = "3px solid blue";
     buttonPressed=4;
}
function onAddArmyClick(){
    notClickedButtons();
     buttonPressed=5;
    (document.getElementById("addArmy")).style.border = "3px solid blue";
}
function onRetireClick(){
     notClickedButtons();
     buttonPressed=6;
    (document.getElementById("retire")).style.border = "3px solid blue";
}
function onConfirmClick(){
    notClickedButtons();
    var params = {};
    (document.getElementById("confirm")).style.border = "3px solid blue";
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
