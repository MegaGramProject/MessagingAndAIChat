<script setup>
defineProps({
convoId: {
    type: String,
    required: true
},
togglePublicLinkCreatedPopup: {
    type: Function,
    required: true
}
})
</script>

<template>
<div :style="{backgroundColor:'white', boxShadow:'0 4px 8px 0 rgba(0, 0, 0, 0.2)', position:'absolute', left:'36%', top:'25%',
width:'31em', height:'25em', padding:'1em 0.9em', borderRadius:'5px', zIndex:'10'}">
<p :style="{fontWeight:'bold'}">Public Link Created</p>
<hr :style="{color:'lightgray', marginTop:'1em'}">
<br/>
<p>A public link to your chat has been created.</p>
<br/>
<div :style="{display:'flex', gap:'1em'}">
<input type="checkbox" v-model="isChecked" :style="{marginLeft:'0em', cursor:'pointer'}">
<div :style="{display:'flex', flexDirection:'column', alignItems:'start'}">
<p :style="{fontWeight:'bold'}">Make this chat discoverable</p>
<p :style="{fontSize:'0.85em'}">Allows it to be shown in web searches</p>
</div>
</div>
<div :style="{borderStyle:'solid', borderColor:'lightgray', borderRadius:'1em', height:'4em', padding:'0.4em 1em', marginTop:'1em',
display:'flex', justifyContent:'space-between', alignItems:'center'}">
    <p>http://localhost:8007/publiclySharedAIConvo/{{ convoId }}</p>
    <button @click="copyToClipboard" :style="{backgroundColor:'black', color:'white', borderRadius:'2em', width:'8.8em', display:'flex', alignItems:'center', cursor:'pointer',
    height:'3em', marginLeft:'-3em', marginTop:'3em'}">
        <img :src="copyIcon" :style="{height:'1.9em', width:'1.9em', objectFit:'contain'}"/>
        <span v-if="!copySuccess" :style="{fontWeight:'bold'}">Copy link</span>
        <span v-if="copySuccess" :style="{fontWeight:'bold'}">Successfully Copied</span>
    </button>
</div>
<div :style="{display:'flex', justifyContent:'space-evenly', alignItems:'center', marginTop:'2em'}">
    <img @click="shareChatViaLinkedin" :src="linkedinIcon" :style="{objectFit:'contain', width:'4em', height:'4em', cursor:'pointer'}"/>
    <img @click="shareChatViaFacebook" :src="facebookIcon" :style="{objectFit:'contain', width:'4em', height:'4em', cursor:'pointer'}"/>
    <img @click="shareChatViaReddit" :src="redditIcon" :style="{objectFit:'contain', width:'4em', height:'4em', cursor:'pointer'}"/>
    <img @click="shareChatViaTwitter" :src="twitterIcon" :style="{objectFit:'contain', width:'4em', height:'4em', cursor:'pointer'}"/>
</div>
<img @click="togglePublicLinkCreatedPopup(convoId)" :src="xIcon" :style="{height:'3em', width:'3em', position:'absolute', top:'0%', left:'87%', cursor:'pointer'}"/>
</div>
</template>


<script>
import xIcon from '@/assets/images/xIcon.png';
import copyIcon from '@/assets/images/copyIcon.png';
import linkedinIcon from '@/assets/images/linkedinIcon.png';
import facebookIcon from '@/assets/images/facebookIcon.png';
import redditIcon from '@/assets/images/redditIcon.png';
import twitterIcon from '@/assets/images/twitterIcon.png';


export default {
    data() {
        return {
            isChecked: false,
            copySuccess: false
        };
    },
    methods: {
        shareChatViaLinkedin() {
            window.location.href = "https://linkedin.com";
        },
        shareChatViaFacebook() {
            window.location.href = "https://facebook.com";
        },
        shareChatViaReddit() {
            window.location.href = "https://reddit.com";
        },
        shareChatViaTwitter() {
            window.location.href = "https://twitter.com";
        },
        copyToClipboard() {
            navigator.clipboard.writeText('http://localhost:8007/publiclySharedAIConvo/'+this.convoId).then(() => {
                this.copySuccess = true;
                setTimeout(() => {
                this.copySuccess = false;
                }, 650);
            }).catch(err => {
                console.error('Failed to copy text: ', err);
            });
        },
    }
}
</script>