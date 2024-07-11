<script setup>
defineProps({
sender: {
    type: String,
    required: true
},
message: {
    type: String,
    required: true
}
})
</script>


<template>

    <template v-if="senderIsUser">
    <div :style="{display:'flex', justifyContent:'end', alignItems:'center', fontSize:'1.3em', color:'black', gap:'0.4em'}">
    <img :src="editIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
    <p :style="{backgroundColor:'#ededed', padding:'0.5em 0.5em', borderRadius:'1em', fontSize:'0.85em', wordBreak: 'break-all',
    overflowWrap: 'break-word', maxWidth:'65%'}">{{ message }}</p>
    </div>
    </template>

    <template v-if="!senderIsUser">
    <div :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', fontSize:'1.3em', color:'black',
    position:'relative'}">
    <p :style="{fontSize:'0.85em'}">{{ message }}</p>
    <img :src="chatgptIcon" :style="{position:'absolute', top:'-10%', left:'-5.4%', objectFit:'contain', height:'2em', width:'2em', zIndex:'10',
    pointerEvents:'none'}"/>
    <div :style="{display:'flex', gap:'0.3em', marginTop:'0.6em'}">
        <img :src="readAloudIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img :src="copyPasteIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img :src="regenerateIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
    </div>
    </div>
    </template>

</template>

<script>
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import copyPasteIcon from '@/assets/images/copyPasteIcon.png';
import editIcon from '@/assets/images/editIcon.png';
import readAloudIcon from '@/assets/images/readAloudIcon.png';
import regenerateIcon from '@/assets/images/regenerateIcon.png';
export default {
    data() {
        return {
            senderIsUser: this.sender === 'user' ? true : false
        }
    }
}

</script>