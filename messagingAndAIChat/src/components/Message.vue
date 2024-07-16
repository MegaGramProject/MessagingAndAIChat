<script setup>
defineProps({
sender: {
    type: String,
    required: true
},
message: {
    type: String,
    required: true
},
messageId: {
    type: String,
    required: true
},
messageFiles: {
    type: Array,
    required: true
},
accent: {
    type: String,
    required: true
},
voiceSpeed: {
    type: String,
    required: true
},
voiceType: {
    type: String,
    required: true
}
})
</script>


<template>

    <template v-if="senderIsUser && !isDeleted">
    <div :style="{display:'flex', flexDirection:'column', alignItems:'end', gap:'1.4em'}">
    
    <div v-if="messageFileImages.length>0" v-for="(fileImage, index) in messageFileImages" :key="index" :style="{display:'flex', flexDirection:'column'}">
    <div :style="{display: 'flex', alignItems:'center', gap:'1.6em'}">
    <img @click="downloadFile(messageFiles[index])" :src="fileImage" :style="{width:'3em', height:'3em', objectFit:'contain', cursor:'pointer'}"/>
    <p :style="{fontSize:'0.67em'}">{{ messageFileNames[index] }}</p>
    </div>
    </div>

    <div v-if="!editMode" :style="{display:'flex', justifyContent:'end', alignItems:'center', fontSize:'1.3em', color:'black', gap:'0.4em'}">
    <img @click="deleteMessage" :src="deleteMessageIcon" :style="{objectFit:'contain', cursor:'pointer', height:'0.5em', width:'0.5em'}"/>
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

    </div>

    </template>

    <template v-if="!senderIsUser && !isDeleted">
    <div :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', fontSize:'1.3em', color:'black',
    position:'relative'}">
    <p :style="{fontSize:'0.85em'}">{{ messageDisplayed }}</p>
    <img :src="chatgptIcon" :style="{position:'absolute', top:'-10%', left:'-5.4%', objectFit:'contain', height:'2em', width:'2em', zIndex:'10',
    pointerEvents:'none'}"/>
    <div :style="{display:'flex', gap:'0.3em', marginTop:'0.6em', alignItems:'center'}">
        <img v-if="!audioPlaying" @click="readAloudMessage" :src="readAloudIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img v-if="audioPlaying" @click="stopAudio" :src="stopReadAloudIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img @click="copyToClipboard" :src="copyPasteIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img @click="regenerateMessage" :src="regenerateIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1.3em', width:'1.3em'}"/>
        <img @click="deleteMessage" :src="deleteMessageIcon" :style="{objectFit:'contain', cursor:'pointer', height:'1em', width:'1em'}"/>

    </div>
    <p v-if="copySuccess" :style="{fontSize:'0.73em'}">Successfully copied message</p>
    </div>
    </template>

</template>

<script>
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import copyPasteIcon from '@/assets/images/copyPasteIcon.png';
import editIcon from '@/assets/images/editIcon.png';
import fileImage from '@/assets/images/fileImage.png';
import readAloudIcon from '@/assets/images/readAloudIcon.png';
import regenerateIcon from '@/assets/images/regenerateIcon.png';
import deleteMessageIcon from '@/assets/images/grayTrashIcon.png';
import stopReadAloudIcon from '@/assets/images/stopReadAloudIcon.png';

export default {
    data() {
        return {
            senderIsUser: this.sender === 'user' ? true : false,
            copySuccess: false,
            messageDisplayed: this.message,
            editMode: false,
            messageBeforeEdit: "",
            messageFileImages: [],
            messageFileNames: [],
            isDeleted: false,
            audio: null,
            audioPlaying: false
        }
    },
    mounted() {
        this.messageFileImages = [];
        this.messageFileNames = [];
        if(this.messageFiles) {
            const allowedImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/webp', 'image/svg+xml'];

            for (let i = 0; i < this.messageFiles.length; i++) {
                let currElem = this.messageFiles[i];
                if (allowedImageTypes.includes(currElem.type)) {
                    this.messageFileImages.push(URL.createObjectURL(currElem));
                } else {
                    this.messageFileImages.push(fileImage);
                }
                this.messageFileNames.push(currElem.name);
            }
        }
    },
    methods: {
        async deleteMessage() {
            this.isDeleted = true;
            const response = await fetch('http://localhost:8008/api/aimessage/'+ this.messageId, {
                method: 'DELETE'
            });
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
        },
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
        async regenerateMessage() {
            this.messageDisplayed = "Message has been regenerated.";
            const options = {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        'message': this.messageDisplayed
                    })
                };
            const response = await fetch('http://localhost:8008/api/aimessage/'+this.messageId, options);
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
        },
        async toggleEditMode() {
            if(!this.editMode) {
                this.messageBeforeEdit = this.messageDisplayed;
                this.editMode = true;
            }
            else {
                const options = {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        'message': this.messageDisplayed
                    })
                };
                
                const response = await fetch('http://localhost:8008/api/aimessage/'+this.messageId, options);
                if(!response.ok) {
                    throw new Error('Network response not ok');
                }
                
                this.messageBeforeEdit = "";
                this.editMode = false;
            }
        },

        async readAloudMessage() {
            this.audioPlaying = true;
            if(this.audio!==null){
                this.audio.play();
                this.audio.onended = () => {
                    this.audioPlaying = false;
                };
                return;
            }
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'message': this.messageDisplayed,
                    'accent': this.accent,
                    'voiceSpeed': this.voiceSpeed,
                    'voiceType': this.voiceType
                    })
            };
            const response = await fetch('http://localhost:8008/api/spokenAIMessage', options);
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
            const spokenAIMessageBlob = await response.blob();
            const spokenAIMessageURL = URL.createObjectURL(spokenAIMessageBlob);
            const spokenAIMessage = new Audio(spokenAIMessageURL);
            this.audio = spokenAIMessage;
            this.audio.play();
            this.audio.onended = () => {
                this.audioPlaying = false;
            };
            
        },

        cancelEdit() {
            this.messageDisplayed = this.messageBeforeEdit;
            this.messageBeforeEdit = "";
            this.editMode = false;
        },

        stopAudio() {
            this.audioPlaying = false;
            if(this.audio){
                this.audio.pause();
                this.audio.currentTime = 0;
            }
        },
        downloadFile(file) {
            const url = URL.createObjectURL(file);
            const a = document.createElement('a');
            a.href = url;
            a.download = file.name;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }
    },
    watch: {
        accent(newVal) {
            if(this.audio!==null) {
                this.audio.pause();
                this.audio = null;
            }
        },
        voiceSpeed(newVal) {
            if(this.audio!==null) {
                this.audio.pause();
                this.audio = null;
            }
        },
        voiceType(newVal) {
            if(this.audio!==null) {
                this.audio.pause();
                this.audio = null;
            }
        }
    }
}

</script>