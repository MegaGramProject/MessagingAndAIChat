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
}
})
</script>


<template>

    <template v-if="senderIsUser">
    <div class="userMessage" :style="{display:'flex', flexDirection:'column', alignItems:'end', gap:'1.4em'}">
    <div v-if="messageFileImages.length>0" v-for="(fileImage, index) in messageFileImages" :key="index" :style="{display:'flex', flexDirection:'column'}">
    <div :style="{display: 'flex', alignItems:'center', gap:'1.6em'}">
    <img @click="downloadFile(messageFiles[index])" :src="fileImage" :style="{width:'3em', height:'3em', objectFit:'contain', cursor:'pointer'}"/>
    <p :style="{fontSize:'0.67em'}">{{ messageFileNames[index] }}</p>
    </div>
    </div>
    <p :style="{backgroundColor:'#ededed', padding:'0.5em 0.5em', borderRadius:'1em', fontSize:'1.2em', wordBreak: 'break-all',
    overflowWrap: 'break-word', maxWidth:'65%'}">{{ message }}</p>
    </div>
    </template>


    <template v-if="!senderIsUser">
    <div class="aiReply" :style="{display:'flex', flexDirection:'column', alignItems:'start', justifyContent:'start', fontSize:'1.3em', color:'black',
    position:'relative'}">
    <p :style="{fontSize:'1em'}">{{ message }}</p>
    <img :src="chatgptIcon" :style="{position:'absolute', top:'-10%', left:'-5.4%', objectFit:'contain', height:'2em', width:'2em', zIndex:'10',
    pointerEvents:'none'}"/>
    </div>
    </template>

</template>

<script>
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import fileImage from '@/assets/images/fileImage.png';
import '@/assets/styles.css';


export default {
    data() {
        return {
            senderIsUser: this.sender === 'user' ? true : false,
            messageFileImages: [],
            messageFileNames: [],
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
    }
}

</script>