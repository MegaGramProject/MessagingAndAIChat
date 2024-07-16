<script setup>
import PublicMessage from './PublicMessage.vue';
defineProps({
messages: {
    type: Array,
    required: true
}
})
</script>


<template>
<div ref="scrollableDiv" :style="{backgroundColor:'white', position:'absolute', display:'flex', flexDirection:'column',
    left:'25%', top:'19%', width:'54%', height:'65%', padding:'2em 3em', overflowY:'scroll', gap:'2.85em'}">
    <template v-for="(message, index) in messages" :key="index">
    <PublicMessage :sender="message[0]" :message="message[1]" :messageId="message[2]" :messageFiles="message[3]"/>
    </template>
</div>

</template>


<script>
import '@/assets/styles.css';
export default {
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