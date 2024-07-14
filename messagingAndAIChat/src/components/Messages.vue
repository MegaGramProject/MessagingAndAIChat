<script setup>
import Message from './Message.vue';
defineProps({
messages: {
    type: Array,
    required: true
},
setInput: {
    type: Function,
    required: true
}
})
</script>


<template>
<div ref="scrollableDiv" v-if="messagesExist" :style="{backgroundColor:'white', position:'absolute', display:'flex', flexDirection:'column',
    left:'25%', top:'9.4%', width:'54%', height:'69%', padding:'2em 3em', overflowY:'scroll', gap:'2.85em'}">
    <template v-for="(message, index) in messages" :key="index">
    <Message :sender="message[0]" :message="message[1]" :messageId="message[2]"/>
    </template>
</div>

<div v-if="!messagesExist" :style="{backgroundColor:'white', position:'absolute', display:'flex',
flexDirection:'column', left:'25%', top:'4%', width:'54%', height:'69%', justifyContent:'center', alignItems:'center'}">
    <img :src="chatgptIcon" :style="{height:'6em', width:'6em', objectFit:'contain', pointerEvents:'none'}"/>
    <br/>
    <div :style="{display:'flex', gap:'0.6em'}">
        <div @click="setInput('Design a fun coding game')" class="hoverableElement" :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', width:'9em',
        borderStyle:'solid', borderColor:'lightgray', borderRadius:'8px', padding:'0.3em 0.5em', cursor:'pointer'}">
        <img :src="lightbulbIcon" :style="{height:'1.3em', width:'1.3em', objectFit:'contain'}"/>
        <p>Design a fun coding game</p>
        </div>

        <div @click="setInput('Fun fact about the Roman Empire')" class="hoverableElement" :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', width:'9em',
        borderStyle:'solid', borderColor:'lightgray', borderRadius:'8px', padding:'0.3em 0.5em', cursor:'pointer'}">
        <img :src="funFactIcon" :style="{height:'1.3em', width:'1.3em', objectFit:'contain'}"/>
        <p>Fun fact about the Roman Empire</p>
        </div>

        <div @click="setInput('Thank my interviewer')" class="hoverableElement" :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', width:'9em',
        borderStyle:'solid', borderColor:'lightgray', borderRadius:'8px', padding:'0.3em 0.5em', cursor:'pointer'}">
        <img :src="appreciationIcon" :style="{height:'1.3em', width:'1.3em', objectFit:'contain'}"/>
        <p>Thank my interviewer</p>
        </div>

        <div @click="setInput('Explain nostalgia to a kindergartener')" class="hoverableElement" :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', width:'9em',
        borderStyle:'solid', borderColor:'lightgray', borderRadius:'8px', padding:'0.3em 0.5em', cursor:'pointer'}">
        <img :src="educationalIcon" :style="{height:'1.3em', width:'1.3em', objectFit:'contain'}"/>
        <p>Explain nostalgia to a kindergartener</p>
        </div>
    </div>
</div>
</template>


<script>
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import lightbulbIcon from '@/assets/images/lightbulbIcon.png';
import funFactIcon from '@/assets/images/funFactIcon.png';
import appreciationIcon from '@/assets/images/appreciationIcon.png';
import educationalIcon from '@/assets/images/educationalIcon.png';
import '@/assets/styles.css';
export default {
    computed: {
        messagesExist() {
            return this.messages.length > 0;
        }
    },
    updated() {
        if(this.messages.length>0) {
            this.scrollToBottom();
        }
    },
    methods: {
        scrollToBottom() {
            const container = this.$refs.scrollableDiv;
            container.scrollTop = container.scrollHeight;
        }
    }
}
</script>