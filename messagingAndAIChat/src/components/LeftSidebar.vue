<script setup>
import PastConvosGroup from './PastConvosGroup.vue'
defineProps({
toggleLeftSidebar: {
    type: Function,
    required: true
},
toggleShareChatPopup: {
    type: Function,
    required: true
},
selectedConvo: {
    type: Number,
    required: true
},
selectNewConvo: {
    type: Function,
    required: true
},
createNewConvo: {
    type: Function,
    required: true
}
})
</script>


<template>
<div :style="{position:'fixed', width:'16em', top:'0%', left:'0%', height:'100%',
display:'flex', flexDirection:'column', backgroundColor:'#f7f7f7', padding: '1em 0.7em', overflowY:'scroll'}">
<div :style="{display:'flex', justifyContent:'space-between'}">
<img @click="toggleLeftSidebar()" @contextmenu.prevent="disableRightClick" :src="toggleSidebarIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em',
objectFit:'contain'}" @mouseover="onSidebarIconHover" @mouseleave="onSidebarIconHover">
<img @click="createNewConvo()" :src="newChatIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em', objectFit:'contain'}"
@mouseover="onNewChatIconHover" @mouseleave="onNewChatIconHover">
</div>
<div @contextmenu.prevent="disableRightClick" :style="{display:'flex', justifyContent:'space-between', marginTop:'0.4em'}">
<p :style="{backgroundColor:'black', color:'white', width:'45%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.6em', display: displayShowCloseSidebarText}">Close sidebar</p>
<p :style="{backgroundColor:'black', color:'white', width:'43%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.4em', marginLeft:'13.5em', display: displayShowNewChatText}">New chat</p>
</div>

<div @click="createNewConvo()" class="hoverableElement" :style="{display:'flex', width:'100%', borderRadius:'8px', height:'3.4em', alignItems:'center', cursor:'pointer'}">
<img :src="chatgptIcon" :style="{height:'2.4em', width:'2.4em', objectFit:'contain'}"/>
<p :style="{marginLeft:'1em'}">MegAI</p>
</div>

<br/>
<br/>
<br/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-07-09')" :selectedConvo="selectedConvo" :convoTitles="[['Convo3- How to drive a car', 0]]"
:selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
<br/>
<br/>
<br/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-07-08')" :selectedConvo="selectedConvo" :convoTitles="[['Convo3- How to drive a car', 3], ['Convo2- How to horse-ride', 4], ['Convo1- How to time-travel',5]]"
:selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-06-01')" :selectedConvo="selectedConvo" :convoTitles="[['Convo3- How to drive a car', 6], ['Convo2- How to horse-ride', 7], ['Convo1- How to time-travel',8]]"
:selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-02-12')" :selectedConvo="selectedConvo" :convoTitles="[['Convo3- How to drive a car', 9], ['Convo2- How to horse-ride', 10], ['Convo1- How to time-travel',11]]"
:selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2023-05-19')" :selectedConvo="selectedConvo" :convoTitles="[['Convo3- How to drive a car', 12], ['Convo2- How to horse-ride', 13], ['Convo1- How to time-travel',14]]"
:selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
</div>
</template>


<script>
import toggleSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import newChatIcon from '@/assets/images/newChatIcon.png';
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import '@/assets/styles.css';
export default {
data() {
    return {
        toggleSidebarIcon,
        newChatIcon,
        showCloseSidebarText: false,
        showNewChatText: false,
    };
},

methods: {
    disableRightClick(event) {
        event.preventDefault();
    },
    onSidebarIconHover() {
        this.showCloseSidebarText = !this.showCloseSidebarText;
    },
    onNewChatIconHover() {
        this.showNewChatText = !this.showNewChatText;
    }
},

computed: {
    displayShowCloseSidebarText(){
        return this.showCloseSidebarText ? 'inline-block' : 'none';
    },
    displayShowNewChatText(){
        return this.showNewChatText ? 'inline-block' : 'none';
    }

}

};
</script>