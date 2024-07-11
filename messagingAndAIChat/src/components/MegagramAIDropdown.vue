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
}
})
</script>


<template>
<div :style="{backgroundColor:'white', display:'flex', gap:'1em', position:'absolute', 'top':'10%', left:'19.3%',
borderStyle:'solid', borderColor:'lightgray', borderRadius:'6.9px', width:'20em', borderWidth:'0.01em', height:'3em',
alignItems:'center', justifyContent:'space-between'}">
    <div :style="{display:'flex', gap:'0.5em', marginLeft:'0.6em'}">
    <img :src="temporaryChatIcon" :style="{height:'1.7em', width:'1.7em', objectFit:'contain'}"/>
    <p>Temporary Chat</p>
    </div>
    <template v-if="!isTemporary">
    <img @click="toggleTemporaryChat" :src="toggleSwitchLeft" :style="{height:'3em', width:'3em', objectFit:'contain', marginRight:'1em', cursor:'pointer'}"/>
    </template>
    <template v-if="isTemporary">
    <img @click="toggleTemporaryChat" :src="toggleSwitchRight" :style="{height:'3em', width:'3em', objectFit:'contain', marginRight:'1em', cursor:'pointer'}"/>
    </template>
</div>
</template>


<script>
import temporaryChatIcon from '@/assets/images/temporaryChatIcon.png';
import toggleSwitchLeft from '@/assets/images/toggleSwitchLeft.png';
import toggleSwitchRight from '@/assets/images/toggleSwitchRight.png';

export default {
    data() {
        return {
            temporaryMode: this.isTemporary
        }
    }
}
</script>