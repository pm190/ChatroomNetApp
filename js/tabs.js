window.onload=function() {
  setUpTabs()

  }

function setUpTabs(){
  // get all the tab headers
  var tabHeaders = document.getElementsByClassName('clickTab'); 
  // hid all the tab contents
  // hideTabs('tabContent');
  // set a on click event for all tab headers
  for(index = 0; index < tabHeaders.length; ++index)
  {
    tabHeaders[index].addEventListener("click", displayTab, false);
  }
}

function displayTab(id)
{
  //before displaying hide all tabs and effectively an other tabs that have been clicked
  hideTabs('tabContent');
  var tabID = this.id.split("_")[1];
  var tab = document.getElementById(tabID);
  tab.style.display="inline-block";
}

function hideTabs(id)
{
  var tabContents = document.getElementsByClassName(id);  
  // set a on click event for all tab headers
  for(index = 0; index < tabContents.length; ++index)
  {
    tabContents[index].style.display = "none";
  }
}
// function setUpTabs(tab-container-id){

//    // get tab container
//    var container = document.getElementsByName(tab);
//     // set current tab
//     var navitem = container.querySelector(".tabs ul li");
//     //store which tab we are on
//     var ident = navitem.id.split("_")[1];
//     navitem.parentNode.setAttribute("data-current",ident);
//     //set current tab with class of activetabheader
//     navitem.setAttribute("class","tabActiveHeader");

//     //hide two tab contents we don't need
//     var pages = container.querySelectorAll(".tabpage");
//     for (var i = 1; i < pages.length; i++) {
//       pages[i].style.display="none";
//     }

//     //this adds click event to tabs
//     var tabs = container.querySelectorAll(".tabs ul li");
//     for (var i = 0; i < tabs.length; i++) {
//       tabs[i].onclick=displayPage;
//     }
// }

// // on click of one of tabs
// function displayPage() {
//   var current = this.parentNode.getAttribute("data-current");
//   //remove class of activetabheader and hide old contents
//   document.getElementById("tabHeader_" + current).removeAttribute("class");
//   document.getElementById("tabpage_" + current).style.display="none";

//   var ident = this.id.split("_")[1];
//   //add class of activetabheader to new active tab and show contents
//   this.setAttribute("class","tabActiveHeader");
//   document.getElementById("tabpage_" + ident).style.display="block";
//   this.parentNode.setAttribute("data-current",ident);
// }
  