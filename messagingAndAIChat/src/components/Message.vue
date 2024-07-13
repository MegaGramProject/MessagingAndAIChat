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
    <div v-if="!editMode" :style="{display:'flex', justifyContent:'end', alignItems:'center', fontSize:'1.3em', color:'black', gap:'0.4em'}">
    <img @click="toggleEditMode" :src="editIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
    <p :style="{backgroundColor:'#ededed', padding:'0.5em 0.5em', borderRadius:'1em', fontSize:'0.85em', wordBreak: 'break-all',
    overflowWrap: 'break-word', maxWidth:'65%'}">{{ messageDisplayed }}</p>
    </div>

    <div v-if="editMode" :style="{width:'100%', backgroundColor:'#f7f7f7', borderRadius:'6px', fontFamily:'Arial', padding: '0.9em 0.9em'}">
    <textarea ref="editMessage" v-model="messageDisplayed" :style="{fontFamily:'Arial', width:'95%', backgroundColor:'#f7f7f7', outline:'none', border:'none',
    fontSize:'1.1em'}"/>
    <div :style="{display:'flex', justifyContent:'end', gap:'0.35em'}">
        <button @click="cancelEdit" :style="{backgroundColor:'white', borderRadius:'2em', padding:'0.5em 0.5em', fontWeight:'semibold', cursor:'pointer'}">Cancel</button>
        <button @click="toggleEditMode" :style="{backgroundColor:'black', color: 'white', borderRadius:'2em', padding:'0.5em 0.5em', fontWeight:'semibold', cursor:'pointer'}">Send</button>
    </div>
    </div>

    </template>

    <template v-if="!senderIsUser">
    <div :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', fontSize:'1.3em', color:'black',
    position:'relative'}">
    <p :style="{fontSize:'0.85em'}">{{ messageDisplayed }}</p>
    <img :src="chatgptIcon" :style="{position:'absolute', top:'-10%', left:'-5.4%', objectFit:'contain', height:'2em', width:'2em', zIndex:'10',
    pointerEvents:'none'}"/>
    <div :style="{display:'flex', gap:'0.3em', marginTop:'0.6em'}">
        <img :src="readAloudIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img @click="copyToClipboard" :src="copyPasteIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img @click="regenerateMessage" :src="regenerateIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
    </div>
    <p v-if="copySuccess" :style="{fontSize:'0.73em'}">Successfully copied message</p>
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
            senderIsUser: this.sender === 'user' ? true : false,
            copySuccess: false,
            messageDisplayed: this.message,
            editMode: false,
            messageBeforeEdit: ""
        }
    },
    methods: {
        copyToClipboard() {
            navigator.clipboard.writeText(this.messageDisplayed).then(() => {
                this.copySuccess = true;
                setTimeout(() => {
                this.copySuccess = false;
                }, 650);
            }).catch(err => {
                console.error('Failed to copy text: ', err);
            });
        },
        regenerateMessage() {
            this.messageDisplayed = "Message has been regenerated.";
        },
        toggleEditMode() {
            if(!this.editMode) {
                this.messageBeforeEdit = this.messageDisplayed;
                this.editMode = true;
            }
            else {
                this.messageBeforeEdit = "";
                this.editMode = false;
            }
        },
        cancelEdit() {
            this.messageDisplayed = this.messageBeforeEdit;
            this.messageBeforeEdit = "";
            this.editMode = false;
        }
    }
}

</script>