window.onload=function() {
  setUpTabs()

  }

function setUpTabs(){
  // get all the tab headers
  var tabHeaders = document.getElementsByClassName('clickTab'); 
  // get the active headers specified
  var activeHeader = document.getElementsByClassName('activeHeader');
  activeHeader.style.display = "inline-block";
  for(index = 0; index < tabHeaders.length; ++index)
  {
    tabHeaders[index].addEventListener("click", displayTab, false);
  }
}

function displayTab(id)
{
  //remove any active headers
  removeActive(this.parentNode.id);

  //set this header as activeheader
  this.className = "clickTab activeHeader";
  //work out the tab content id from last part of the tab header id
  var tabID = this.id.split("_")[1];

  var tab = document.getElementById(tabID);
  // hide all tabls
  hideTabs(tab.parentNode.id);

  // if this is the left tab list we just display data
  // if it is the right tab list we display the chat in the text area
  if(this.parentNode.id === 'leftTabList')
  {
    tab.style.display="inline-block";
  }
  else if(this.parentNode.id === 'rightTabList')
  {
    displayChat(tab.innerHTML);
  }
}

function hideTabs(id)
{
  var tabContents = document.getElementById(id).getElementsByTagName('div'); 
  // set a on click event for all tab headers
  for(index = 0; index < tabContents.length; ++index)
  {
    tabContents[index].style.display = "none";
  }
}

//get id using id find all childer li and set their class to 'clickTab'
//essentially removing any active tabs
function removeActive(id)
{
  var tabHeaders = document.getElementById(id).getElementsByTagName('li'); 
  for(index = 0; index < tabHeaders.length; ++index)
  {
    tabHeaders[index].className = "clickTab";
  }
}

function displayChat(data)
{
  var chat = document.getElementById('chat');
  chat.innerHTML = data;
}