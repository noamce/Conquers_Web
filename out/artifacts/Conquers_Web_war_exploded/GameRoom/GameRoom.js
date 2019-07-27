
var userName;
var targetTerritory;
var engineStart=false;
var buttonPressed;
var dataTableSize;
var territoryDataTableSize;
var showTerritoryInfoFlag=false;
var currentPlayerPlaying;
var refreshRate = 2000;
var roundNumber=1;
var gameCanStart=false;
var checkIfItIsThisUserTurn=false;
var once=true;
var initHappend=true;
var isItFirstRound;
var confirmPressed=false;



window.onload=function ()
 {
     turnOffButtons();
     document.getElementById("retire").style.visibility="hidden";
     document.getElementById("endTurn").style.visibility="hidden";
     setInterval(intevalInit,refreshRate);
    // setGameForFirstTime();
     //setInterval(checkGamecanStart,refreshRate);
     //


     //createGameDetails();
     //getCurrentPlayerInfo();



 };
function intevalInit() {
    if(initHappend)
    {
        setGameForFirstTime();
        initHappend=false;
    }
    else{

        setInterval(getAllData,refreshRate);
        if(gameCanStart){
            setInterval(displayingIt,refreshRate);
        }
    }

}

function getAllData(){
    checkGamecanStart();
    checkIfPLAYERCanPlay();
}
function checkIfPLAYERCanPlay() {
    $.ajax
    ({
        url: '/IsItThisPlayer',
        data: {
            action: "isItThisPlayer"
        },
        type: 'GET',
        success: checkingPlayer

    });

}
function checkingPlayer(currentPlayer) {

    currentPlayerPlaying=currentPlayer.toString();

    if(currentPlayer.toString() === userName) {
        checkIfItIsThisUserTurn = true;
    }
    else {
        checkIfItIsThisUserTurn = false;
        confirmPressed=false;
    }

}
function checkGamecanStart(){
    $.ajax
    ({
        url: '/AllPlayersHere',
        data: {
            action: "allPlayersHere"
        },
        type: 'GET',
        success: allOfthePlayersHers

    });
}

function allOfthePlayersHers(allPlayersHere) {
    if(allPlayersHere && once){
        once=false;
        gameCanStart=true;
        document.getElementById('leaveButton').style.visibility='hidden';
        startGame();
        alert("The Game started")
    }

}
function displayingIt(){

    getCurrentPlayerInfo();

    if(checkIfItIsThisUserTurn) {
        $('.gameStatus').text("Your Turn");
        upDateMapandInfo();


    }
    else
    {
        $("#boardBody").empty();
        document.getElementById("retire").style.visibility="hidden";
        document.getElementById("endTurn").style.visibility="hidden";
        turnOffButtons();
        $('.gameStatus').text("Wait For Your Turn");
    }

}
function setGameForFirstTime() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "initCall"
        },
        type: 'GET',
        success: initSet

    });

}
function initSet(nameOfTheSessionPlayer){

    userName = nameOfTheSessionPlayer.toString(); //this is the name of the player who is the page open(of the session)
    $('.userNameSpan').text("hi "+userName);
    $('.gameStatus').text("Welcome To Conquers: Waiting for all Players");

}
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
    //console.log(data);
    currentPlayerPlaying=data.name.toString();
    var element = document.getElementById("nameOfcurrentPlayer");
    element.innerHTML="Player Turn:"+data.name.toString();
    element = document.getElementById("roundNumberinfo");
    element.innerHTML=data.roundNumber;
    roundNumber=data.roundNumber;
    element = document.getElementById("totalCycleinfo");
    element.innerHTML=" / " +data.totalCycles.toString();
    findPlayerColor(data.color);
    element = document.getElementById("TurringsInfo");
    element.innerHTML="Turrings: "+ data.funds;
    element = document.getElementById("numberOfTerritories");
    element.innerHTML="# of Territories : "+ data.numberOfTerritories;
}

function findPlayerColor(color){

    if (color===0) {
        $('.PlayerColor').text("Player color: RED");
        //document.getElementById("PlayerColor").style.color= "red";
        //player_color.setTextFill(Color.RED);
    }
    if (color===1) {
        $('.PlayerColor').text("Player color: BLUE");
       // document.getElementById("PlayerColor").style.color= "blue";
       // player_color.setTextFill(Color.BLUE);
    }
    if (color===2) {
        $('.PlayerColor').text("Player color: GREEN");
        //document.getElementById("PlayerColor").style.color= "green";
        //player_color.setTextFill(Color.GREEN);
    }
    if (color===3) {
        $('.PlayerColor').text("Player color: YELLOW");
       // document.getElementById("PlayerColor").style.color= "yellow";
        //player_color.setTextFill(Color.YELLOW);
    }

}

function findCellColor(color){

    if (color===0) {
        return "red"
        //document.getElementById("PlayerColor").style.color= "red";
        //player_color.setTextFill(Color.RED);
    }
    if (color===1) {
        return "blue"
        // document.getElementById("PlayerColor").style.color= "blue";
        // player_color.setTextFill(Color.BLUE);
    }
    if (color===2) {
        return "green"
        //document.getElementById("PlayerColor").style.color= "green";
        //player_color.setTextFill(Color.GREEN);
    }
    if (color===3) {
        return "yellow"
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
    // document.getElementById('tableData').style.visibility='hidden';
    document.getElementById('confirm').style.visibility='hidden';
    //document.getElementById('togglee').style.visibility = 'visible';



}
function upDateMapandInfo() {
    $.ajax
    ({
        url: '/updateMap',
        data: {
            action: "getGameDetails"
        },
        type: 'GET',
        success: drawBoard

    });

}
// function updateMapAndINFO(data){
//
//         territoriesMap=data.gameEngine.descriptor.territoryMap;
//         drawBoard();
//     }
function startGame() {
    if (engineStart===false)
        {
            $.ajax
            ({
                url: 'SingleGame',
                data: {
                    action: "startGame"

                },
                type: 'GET',
                success: engineStart=true
            });

        }
}
// function createGameDetails() {
//     $.ajax
//     ({
//         url: 'SingleGame',
//         data: {
//             action: "getGameDetails"
//         },
//         type: 'GET',
//         success: setGameDetails
//
//     });
// }

// function setGameDetails(data) {
//     userName = data.userName; //this is the name of the player who is the page open(of the session)
//     $('.userNameSpan').text("hi "+userName);
//     $('.gameStatus').text("the game start");
//     cols = data.cols;
//     rows = data.rows;
//     territoriesMap=data.gameEngine.descriptor.territoryMap;
//     if (data.started===true)
//     {
//         if (engineStart===false)
//         {
//             $.ajax
//             ({
//                 url: 'SingleGame',
//                 data: {
//                     action: "startGame"
//
//                 },
//                 type: 'GET',
//                 success: engineStart=true
//             });
//
//         }
//         alertGameStart++;
//         if (alertGameStart === 1){
//             alert("The game started");
//         }
//         document.getElementById('leaveButton').style.visibility='hidden';
//         getCurrentPlayerInfo();
//         drawBoard();
//         drawUnitTable();
//
//     }
// }

function drawUnitTable()
{
    $.ajax
({
    url: '/DataTable',
    data: {
        action: "dataTableDetails"
    },
    type: 'GET',
    success: drawDataTable

});
}
function drawDataTable(dataTable) {
    //console.log(dataTable);
    $("#dataTable").empty();
    dataTableSize=dataTable.length;
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
          amountInput.setAttribute("value","0");
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

function drawBoard(data)
{
        var cols=data.cols;
        var rows=data.rows;
        var territoriesMap=data.territoryMapToSend;// get the reference for the body
        var div1 = document.getElementById('boardBody');
        $("#boardBody").empty();
        var i=0;
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
                var color = territoriesMap[i].color;
                var cellText = document.createTextNode(id  + threshold  + profit);
                cell.style.color = findCellColor(color);
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
        url: '/Territory',
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
    //console.log(territoryDataTableInfo);
    for ( var i = 0; i < territoryDataTableSize; i++) {
        var tr = document.createElement("tr");
        var td = document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].rank));
        tr.appendChild(td);
        td=document.createElement("td");
        td.appendChild(document.createTextNode(territoryDataTableInfo[i].type));
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
    //territoryButtonInfo=false;
    showTerritoryInfoFlag = false;
    if(confirmPressed) {
        if(territoryButtonInfo.isTerritoryBelongsCurrentPlayer){
            showTerritoryInfoFlag = true;
        }
        else{
            showTerritoryInfoFlag = false;
        }
        document.getElementById("confirm").style.visibility = "hidden";
        document.getElementById("retire").style.visibility = "hidden";
        document.getElementById("endTurn").style.visibility = "visible";
        turnOffButtons();

    }else {
        isItFirstRound = territoryButtonInfo.firstRound;
        if (territoryButtonInfo.firstRound) {
            document.getElementById("retire").style.visibility = "hidden";
            document.getElementById("endTurn").style.visibility = "hidden";
            //check if this is not a conquerd territory
            if (territoryButtonInfo.territoryConquered) {
                turnOffButtons();
                if(territoryButtonInfo.isTerritoryBelongsCurrentPlayer){
                    showTerritoryInfoFlag = true;
                }
                else{
                    showTerritoryInfoFlag = false;
                }
            } else {
                document.getElementById("naturalTerritory").style.visibility = "visible";
                document.getElementById("wellOrchestrated").style.visibility = "hidden";
                document.getElementById("calculatedRisk").style.visibility = "hidden";
                document.getElementById("maintenance").style.visibility = "hidden";
                document.getElementById("addArmy").style.visibility = "hidden";
            }
        } else {
            //if the game is over or there is only one player
            if (territoryButtonInfo.onlyOnePlayer || territoryButtonInfo.gameOver) {
                //turnoffButtons include retire and endturn
                document.getElementById("retire").style.visibility = "hidden";
                document.getElementById("endTurn").style.visibility = "hidden";
                turnOffButtons();
            } else {
                // retire and endturn on
                document.getElementById("retire").style.visibility = "visible";
                document.getElementById("endTurn").style.visibility = "visible";
                //if this territory is conquerd
                if (territoryButtonInfo.territoryConquered) {
                    //if is this  territory is belong to currrent player
                    if (territoryButtonInfo.isTerritoryBelongsCurrentPlayer) {
                        // territory actions on
                        document.getElementById("maintenance").style.visibility = "visible";
                        document.getElementById("addArmy").style.visibility = "visible";
                        document.getElementById("naturalTerritory").style.visibility = "hidden";
                        document.getElementById("wellOrchestrated").style.visibility = "hidden";
                        document.getElementById("calculatedRisk").style.visibility = "hidden";
                        //drawTerritory army table including fp and maintenace
                        showTerritoryInfoFlag = true;
                    } else {
                        showTerritoryInfoFlag = false;
                        //clear the maintence and total firepower of this territory
                        //if this territoryvaild or the currentplayer dont have territories
                        if (territoryButtonInfo.isTargetTerritoryValid || territoryButtonInfo.playerDontHaveTerritories) {
                            // buttons of battles on
                            document.getElementById("naturalTerritory").style.visibility = "hidden";
                            document.getElementById("wellOrchestrated").style.visibility = "visible";
                            document.getElementById("calculatedRisk").style.visibility = "visible";
                            document.getElementById("maintenance").style.visibility = "hidden";
                            document.getElementById("addArmy").style.visibility = "hidden";
                        } else {
                            turnOffButtons();
                        }
                    }
                } else {
                    if (territoryButtonInfo.playerDontHaveTerritories) {
                        //naturalterritory on
                        document.getElementById("naturalTerritory").style.visibility = "visible";
                        document.getElementById("wellOrchestrated").style.visibility = "hidden";
                        document.getElementById("calculatedRisk").style.visibility = "hidden";
                        document.getElementById("maintenance").style.visibility = "hidden";
                        document.getElementById("addArmy").style.visibility = "hidden";
                    } else {
                        if (territoryButtonInfo.isTargetTerritoryValid) {
                            //naturalterritory on
                            document.getElementById("naturalTerritory").style.visibility = "visible";
                            document.getElementById("wellOrchestrated").style.visibility = "hidden";
                            document.getElementById("calculatedRisk").style.visibility = "hidden";
                            document.getElementById("maintenance").style.visibility = "hidden";
                            document.getElementById("addArmy").style.visibility = "hidden";
                        } else {
                            turnOffButtons();
                        }
                    }


                }
            }
        }
    }

}
function territoryClicked() {
    $.ajax
    ({
        url: '/Territory',
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
    drawUnitTable();
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
    document.getElementById("confirm").style.visibility="visible";
}
function onCalculatedRiskClick(){
    notClickedButtons();
    buttonPressed=2;
    (document.getElementById("calculatedRisk")).style.border = "3px solid blue";
    document.getElementById("confirm").style.visibility="visible";
}
function onWellOrchestratedClick(){
    notClickedButtons();
    buttonPressed=3;
    (document.getElementById("wellOrchestrated")).style.border = "3px solid blue";
    document.getElementById("confirm").style.visibility="visible";
}
function onNaturalTerritoryClick(){
    notClickedButtons();
     (document.getElementById("naturalTerritory")).style.border = "3px solid blue";
    document.getElementById("confirm").style.visibility="visible";
     buttonPressed=4;
}
function onAddArmyClick(){
    notClickedButtons();
     buttonPressed=5;
    (document.getElementById("addArmy")).style.border = "3px solid blue";
    document.getElementById("confirm").style.visibility="visible";
}
function onRetireClick(){
     notClickedButtons();
     buttonPressed=6;
    (document.getElementById("retire")).style.border = "3px solid blue";
    document.getElementById("confirm").style.visibility="visible";
}
function onConfirmClick(){
    notClickedButtons();
    (document.getElementById("confirm")).style.border = "3px solid blue";
    if(buttonPressed === 1)
    {
        $.ajax
        ({
            url: 'SingleGame',
            data: {
                action: "maintenance",
                targetTerritory:targetTerritory
            },
            type: 'GET',
            success: checkEnoughTourings

        });
    }
    if(buttonPressed === 2){
        $.ajax
        ({
            url: '/DataTable',
            data: {
                action: "dataTableDetails"
            },
            type: 'GET',
            success: sendCalculatedRisk

        });
    }
    if(buttonPressed === 3)
    {
        $.ajax
        ({
            url: '/DataTable',
            data: {
                action: "dataTableDetails"
            },
            type: 'GET',
            success: sendWellOrchestrated

        });

    }
    if(buttonPressed === 4)
    {
        $.ajax
        ({
            url: '/DataTable',
            data: {
                action: "dataTableDetails"
            },
            type: 'GET',
            success: sendNaturalTerritory

        });

    }
    if(buttonPressed === 5)
    {
        $.ajax
        ({
            url: '/DataTable',
            data: {
                action: "dataTableDetails"
            },
            type: 'GET',
            success: sendAddArmy

        });

    }
    if(buttonPressed === 6)
    {
        $.ajax
        ({
            url: 'SingleGame',
            data: {
                action: "retire",
                player: userName
            },
            type: 'GET'

        });
    }
    confirmPressed=true;
    turnOffButtons();
    document.getElementById("confirm").style.visibility="hidden";
    document.getElementById("endTurn").style.visibility="visible";




//apicall
}
function sendCalculatedRisk(unitTable) {
    var params = {};

    for ( var i = 0; i < unitTable.length; i++) {

        params[unitTable[i].type] =document.getElementById(unitTable[i].type).value;
    }
    params["action"]="naturalTerritory";
    params["targetTerritory"]=targetTerritory;
    $.ajax
    ({
        url: 'SingleGame',
        data:params,
        type: 'POST',
        success: resultCalculatedRisk

    });
}
function resultCalculatedRisk(data) {
    console.log(data);
}
function sendAddArmy(unitTable) {
    var params = {};

    for ( var i = 0; i < unitTable.length; i++) {

        params[unitTable[i].type] =document.getElementById(unitTable[i].type).value;
    }
    params["action"]="naturalTerritory";
    params["targetTerritory"]=targetTerritory;
    $.ajax
    ({
        url: 'SingleGame',
        data:params,
        type: 'POST',
        success: checkEnoughTouringsTobuildArmy
    });

}
function checkEnoughTouringsTobuildArmy() {


}
function onEndTurnClick(){
    document.getElementById("endTurn").style.visibility="hidden";
    //apicall
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "endTurn"
        },
        type: 'GET',
        success: endTurnUpdate

    });

}
function endTurnUpdate(endTurnInfo) {
   if(endTurnInfo.isCycleOver){


       //made status update that change playerturn
       //setDataRight
       //checkTerritoryColors
   }else{
       //made status update that change playerturn
       //setDataRight
   }
   if(endTurnInfo.gameOver){
       if(thereIsWinner){
           alert("The Winner Is: " +endTurnInfo.winnerPlayerName)
           // winner_war.setText("The Winner of the game: " + getWinnerPlayer().getPlayer_name()); //endTurnInfo.winnerPlayerName
           // winner();
       }else{
           //set Tie
           alert("There are no winners: TIE!")

       }
       //GameOver
       document.getElementById("retire").style.visibility="hidden";
       document.getElementById("endTurn").style.visibility="hidden";
       turnOffButtons();
       showTerritoryInfoFlag=false;
       engineStart=false;
       gameCanStart=false;
       checkIfItIsThisUserTurn=false;
       once=true;
       initHappend=true;
       clearGame();


       //send an ending message that redircet the players out the game and clean playerslist and board
   }

}
function clearGame() {
    $.ajax
    ({
        url: 'SingleGame',
        data: {
            action: "clearGame"
        },
        type: 'GET',
        success:  window.location = "../Lobby/lobby.html"

    });

}
function sendNaturalTerritory(unitTable) {

    var params = {};

    for ( var i = 0; i < unitTable.length; i++) {

        params[unitTable[i].type] =document.getElementById(unitTable[i].type).value;
    }
       params["action"]="naturalTerritory";
       params["targetTerritory"]=targetTerritory;

    $.ajax
    ({
            url: 'SingleGame',
            data: params,
            type: 'POST',
            success: ifNaturalTerritoryIsConquered
        }
    );

}
function ifNaturalTerritoryIsConquered(haveEnoughTourings) {

if(haveEnoughTourings){
    //alert("The Territory is now yours");
}
else{
    alert("You dont have enough Tourings");
}

    //document.getElementById("endTurn").style.visibility="visible";



}
function checkEnoughTourings(hasEnoughMoney) {
    if(hasEnoughMoney)
    {
        //drawdatatable
        showTerritoryInfo();
    }
    else
    {
        window.alert("You Dont have enough Money \n Skip Turn");
        //alert("You Dont have enough Money \n Skip Turn");
    }

}
function sendWellOrchestrated(unitTable) {
    var params = {};

    for ( var i = 0; i < unitTable.length; i++) {

        params[unitTable[i].type] =document.getElementById(unitTable[i].type).value;
    }
    params["action"]="naturalTerritory";
    params["targetTerritory"]=targetTerritory;

    $.ajax
    ({
        url: 'SingleGame',
        data:params,
        type: 'POST',
        success: resultWellOrchestrated
    });

}
function resultWellOrchestrated(warZone) {


    $("#AttackTable").empty();
    $('#Winner').empty();
    if(warZone === false){


    }
    else {
        ArmyAttckTableSize = warZone.PreattackerArmy.length;
        //console.log(territoryDataTableInfo);
        for (var i = 0; i < territoryDataTableSize; i++) {
            var tr = document.createElement("tr");
            var td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PreattackerArmy.unitType));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PreattackerArmy.preAmount));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PreattackerArmy.fp));
            tr.appendChild(td);



            document.getElementById("AttackTable").appendChild(tr);
        }
        ArmyAttckTableSize = warZone.PredefenceArmy.length;
        //console.log(territoryDataTableInfo);
        for (var i = 0; i < territoryDataTableSize; i++) {
            var tr = document.createElement("tr");
            var td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PredefenceArmy.unitType));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PredefenceArmy.preAmount));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(warZone.PredefenceArmy.fp));
            tr.appendChild(td);



            document.getElementById("AttackTable").appendChild(tr);
        }
        $('.Winner').text("The Winner is: "+warZone.winner);

    }
}
function deletePlayer() {



}
