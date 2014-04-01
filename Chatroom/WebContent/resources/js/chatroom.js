function handleMessage(chatMessage) {
	var roomName = chatMessage.roomName;
	var chatContent = $(PrimeFaces.escapeClientId('chats:'+roomName+'form:messages'));
	chatContent.append(getChatMessageHTML(chatMessage));
	var activeTabIndex = document.getElementById('chats_activeIndex').value;
	var tabs = document.getElementById('chats').getElementsByTagName('ul')[0].getElementsByTagName('li');
	var activeTabName = tabs[activeTabIndex].getElementsByTagName('a')[0].innerHTML;
	if(activeTabName !== roomName && tabs !== null) {
		for(var i = 0; i < tabs.length; i++) {
			if(tabs[i] !== null && tabs[i].getElementsByTagName('a')[0].innerHTML === roomName) {
					tabs[i].getElementsByTagName('a')[0].style.backgroundColor = 'red';
			}
		}
	}
}

function roomTabChange(index) {
	document.getElementById('chats').getElementsByTagName('ul')[0].getElementsByTagName('li')[index].getElementsByTagName('a')[0].removeAttribute('style');
	renderUserTab();
}

function renderUserTab() {
	var activeTabIndex = document.getElementById('navTabs_activeIndex').value;
	if(activeTabIndex === '1') {
		document.getElementById('renderNavTabsForm:renderNavTabsButton').click();
	}
}

function handleUser(message) {
	renderUserTab();
}

function getChatMessageHTML(chatMessage) {
	var text = chatMessage.text;
	var colourClass = chatMessage.colour;
	return '<span class="'+colourClass+'">'+text+'</span><br/>';
}