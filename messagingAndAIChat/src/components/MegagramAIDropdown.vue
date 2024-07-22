<script setup>
defineProps({
toggleMegagramAIDropdown: {
    type: Function,
    required: true
},
toggleTemporaryChat: {
    type: Function,
    required: true
},
isTemporary: {
    type: Boolean,
    required: true
},
setAccent: {
    type: Function,
    required: true
},
setVoiceSpeed: {
    type: Function,
    required: true
},
setVoiceType: {
    type: Function,
    required: true
}
})
</script>


<template>
<div id="megagramAIDropdown" :style="{backgroundColor:'white', display:'flex', flexDirection:'column', position:'absolute', 'top':'10%', left:'19.3%',
justifyContent:'space-between', zIndex:'10', alignItems:'start', gap:'0.4em'}">
    <div :style="{display:'flex', gap:'0.5em', alignItems:'center', width:'30em', borderStyle:'solid', borderColor:'lightgray',
    borderRadius:'7px', borderWidth:'0.05px'}">
    <img :src="temporaryChatIcon" :style="{height:'1.7em', width:'1.7em', objectFit:'contain', marginRight:'1em'}"/>
    <p>Temporary Chat</p>
    <template v-if="!isTemporary">
    <img @click="toggleTemporaryChat" :src="toggleSwitchLeft" :style="{height:'3em', width:'3em', objectFit:'contain', marginLeft:'7em', cursor:'pointer'}"/>
    </template>
    <template v-if="isTemporary">
    <img @click="toggleTemporaryChat" :src="toggleSwitchRight" :style="{height:'3em', width:'3em', objectFit:'contain', marginLeft:'7em', cursor:'pointer'}"/>
    </template>
    </div>
    <div :style="{display:'flex', alignItems:'center', justifyContent:'space-between', width:'30em', borderStyle:'solid', borderColor:'lightgray',
    borderRadius:'7px', borderWidth:'0.05px', padding:'0.3em 0.3em', height:'3em'}">
        <p>Customize voice</p>
        <select @change="setAccentHere" id="accentOptions" name="accentOptions">
        <option value="accent">Accent</option>
        <option value="american">American</option>
        <option value="australian">Australian</option>
        <option value="british">British</option>
        <option value="indian">Indian</option>
        </select>
        <select @change="setVoiceSpeedHere" id="voiceSpeedOptions" name="voiceSpeedOptions">
        <option value="speed">Voice Speed</option>
        <option value="speed1">1</option>
        <option value="speed0point25">0.25</option>
        <option value="speed0point5">0.5</option>
        <option value="speed1point5">1.5</option>
        <option value="speed2">2</option>
        <option value="speed2point5">2.5</option>
        <option value="speed3">3</option>
        <option value="speed3point5">3.5</option>
        <option value="speed4">4</option>
        </select>
        <select @change="setVoiceTypeHere" id="voiceTypeOptions" name="voiceTypeOptions">
        <option value="voiceType">Voice Type</option>
        <option value="femaleVoice">Female</option>
        <option value="maleVoice">Male</option>
        </select>
    </div>
</div>
</template>


<script>
import temporaryChatIcon from '@/assets/images/temporaryChatIcon.png';
import toggleSwitchLeft from '@/assets/images/toggleSwitchLeft.png';
import toggleSwitchRight from '@/assets/images/toggleSwitchRight.png';
import '@/assets/styles.css';

export default {
    data() {
        return {
            temporaryMode: this.isTemporary
        }
    },
    methods: {
        setAccentHere(event) {
            this.setAccent(event.target.value);
        },
        setVoiceSpeedHere(event) {
            this.setVoiceSpeed(event.target.value);
        },
        setVoiceTypeHere(event) {
            this.setVoiceType(event.target.value);
        }
    }
}
</script>