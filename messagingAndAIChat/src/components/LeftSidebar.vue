<script setup>
import PastConvosGroup from './PastConvosGroup.vue'
</script>


<template>
<div :style="{position:'fixed', width:'16em', top:'0%', left:'0%', height:'100%',
display:'flex', flexDirection:'column', backgroundColor:'#f7f7f7', padding: '1em 0.7em', overflowY:'scroll'}">
<div :style="{display:'flex', justifyContent:'space-between'}">
<img @contextmenu.prevent="disableRightClick" :src="toggleSidebarIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em',
objectFit:'contain'}" @mouseover="onSidebarIconHover" @mouseleave="onSidebarIconHover">
<img :src="newChatIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em', objectFit:'contain'}"
@mouseover="onNewChatIconHover" @mouseleave="onNewChatIconHover">
</div>
<div @contextmenu.prevent="disableRightClick" :style="{display:'flex', justifyContent:'space-between', marginTop:'0.4em'}">
<p :style="{backgroundColor:'black', color:'white', width:'45%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.6em', display: displayShowCloseSidebarText}">Close sidebar</p>
<p :style="{backgroundColor:'black', color:'white', width:'43%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.4em', marginLeft:'13.5em', display: displayShowNewChatText}">New chat</p>
</div>
<br/>
<br/>
<br/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-07-09')" :selectedConvo="selectedConvo"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-07-08')" :selectedConvo="selectedConvo"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-06-01')" :selectedConvo="selectedConvo"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2024-02-12')" :selectedConvo="selectedConvo"/>
<PastConvosGroup :timeOfConvoGroup="new Date('2023-05-19')" :selectedConvo="selectedConvo"/>
</div>
</template>


<script>
import toggleSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import newChatIcon from '@/assets/images/newChatIcon.png';
export default {
data() {
    return {
        toggleSidebarIcon,
        newChatIcon,
        showCloseSidebarText: false,
        showNewChatText: false,
        selectedConvo: 1,
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
    },
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