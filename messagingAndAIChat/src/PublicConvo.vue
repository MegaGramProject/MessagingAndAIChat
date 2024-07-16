<script setup>
import PublicMessages from './components/PublicMessages.vue';
</script>

<template>
    <div :style="{}">
    <div :style="{position:'absolute', top:'5%', left:'31%', display:'flex', flexDirection:'column', alignItems:'start', width:'100%'}">
        <p :style="{fontWeight:'bold', fontSize:'2.4em'}">{{ convotitle }}</p>
        <br/>
        <p :style="{color:'#b5b5b5', fontSize:'1.2em'}">{{ convodates }}</p>
    </div>

    <div :style="{position:'fixed', width:'100%', height:'15%', display:'flex', top:'85%', justifyContent:'center', alignItems:'center'}">
        <button @click="redirectToChat" :style="{backgroundColor:'black', color:'white', borderRadius:'2em',
        fontWeight:'bold', cursor:'pointer', height:'30%', width:'22%', fontSize:'1.2em'}" >Get Started with Megagram AI Chat</button>
    </div>

    <PublicMessages :messages="messages"/>
    </div>
</template>

<script>
export default {
    data() {
    return {
        accent: 'american',
        voiceSpeed: 'speed1',
        voiceType: 'femaleVoice',
        messages: [],
        convoid: "",
        convotitle: "Convo not found",
        convodates: "N/A",
        fetched: false
    }
},
methods: {
    redirectToChat() {
        window.location.href = "http://localhost:8007/aiChat/";
    },
    async fetchMessagesForConvo(convoid) {
        const response = await fetch('http://localhost:8009/getPublicConvoMessages/'+convoid);
        if(!response.ok) {
            throw new Error('Network response not ok');
        }
        const fetchedMessages = await response.json();

        if (fetchedMessages !== "no public convo found") {
            const date1 = fetchedMessages[0][3];
            const date2 = fetchedMessages[fetchedMessages.length-1][3];
            this.convodates = `${this.formatDate(date1)} to ${this.formatDate(date2)}`;
            const response2 = await fetch('http://localhost:8009/getPublicConvoFiles/'+convoid);
            if(!response2.ok) {
                throw new Error('Network response not ok');
            }
            const messageFiles = await response2.json();
            for (let i = 0; i < fetchedMessages.length; i++) {
                    let msg = fetchedMessages[i];
                    let msgFiles = messageFiles.filter(x => x[1] === msg[1]);
                    for (let j = 0; j < msgFiles.length; j++) {
                        const byteArray = this.base64ToUint8Array(msgFiles[j][2]);
                        const blob = new Blob([byteArray]);
                        const file = new File([blob], msgFiles[j][0], { type: msgFiles[j][3] });
                        msgFiles[j] = file;
                    }
                    if (msg[0].startsWith("AI to ")) {
                        this.messages.push(["AI", msg[2], msg[1], msgFiles]);
                    } else {
                        this.messages.push(["user", msg[2], msg[1], msgFiles]);
                    }
            }
        }
    },
    formatDate(dateString) {
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', options);
    },
    base64ToUint8Array(base64) {
        const binaryString = window.atob(base64);
        const len = binaryString.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        return bytes;
  }
},
watch: {
    '$route.params': {
        immediate: true,
        handler(newParams) {
        if(!this.fetched && typeof newParams.convoid !== 'undefined') {
            this.fetched = true;
            this.convoid = newParams.convoid;
            localStorage.setItem('defaultConvo', newParams.convoid);
            this.fetchMessagesForConvo(this.convoid);
        }
        else if(!this.fetched && localStorage.getItem('defaultConvo')) {
            this.fetched = true;
            this.convoid = localStorage.getItem('defaultConvo');
            this.fetchMessagesForConvo(this.convoid);
            }
        }
    }
}
}
</script>