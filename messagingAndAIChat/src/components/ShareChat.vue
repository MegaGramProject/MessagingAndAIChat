<script setup>
defineProps({
convoId: {
    type: String,
    required: true
},
toggleShareChatPopup: {
    type: Function,
    required: true
},
togglePublicLinkCreatedPopup: {
    type: Function,
    required: true
}
})
</script>

<template>
<div id="shareChatPopup" :style="{backgroundColor:'white', boxShadow:'0 4px 8px 0 rgba(0, 0, 0, 0.2)', position:'absolute', left:'42%', top:'37%',
width:'30em', height:'16em', padding:'1em 0.4em', borderRadius:'5px', zIndex:'10'}">
<p :style="{fontWeight:'bold'}">Share public link to chat</p>
<hr :style="{color:'lightgray', marginTop:'1em'}">
<br/>
<p>Your name, custom instructions, and any messages you add after sharing stay private.</p>
<div :style="{borderStyle:'solid', borderColor:'lightgray', borderRadius:'1em', height:'4em', padding:'0.4em 1em', marginTop:'1em',
display:'flex', justifyContent:'space-between', alignItems:'center'}">
    <p>http://localhost:8007/publiclySharedAIConvo/{{ convoId }}</p>
    <button @click="togglePublicLinkCreatedPopup(convoId)" :style="{backgroundColor:'black', color:'white', borderRadius:'2em', width:'8.8em', display:'flex', alignItems:'center', cursor:'pointer',
    height:'3em', marginLeft:'-3em', marginTop:'3em'}">
        <img :src="createLinkIcon" :style="{height:'1.9em', width:'1.9em', objectFit:'contain'}"/>
        <span :style="{fontWeight:'bold'}">Create link</span>
    </button>
</div>
<img class="iconToBeAdjustedForDarkMode" @click="toggleShareChatPopup(convoId)" :src="xIcon" :style="{height:'3em', width:'3em', position:'absolute', top:'0%', left:'87%', cursor:'pointer'}"/>
</div>
</template>


<script>
import createLinkIcon from '@/assets/images/createLinkIcon.png';
import xIcon from '@/assets/images/xIcon.png';
import '@/assets/styles.css';

</script>