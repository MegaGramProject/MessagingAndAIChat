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
        numTimesRouteParamsWatched: 0
    }
},
methods: {
    redirectToChat() {
        window.location.href = "http://localhost:8007/aiChat/";
    },
    async fetchMessagesForConvo(convoid) {
        try {
            const response0 = await fetch('http://localhost:8009/getPublicAIConvoTitle/'+convoid);
            if(!response0.ok) {
                throw new Error('Network not ok');
            }
            const convotitle =  await response0.text();
            this.convotitle = convotitle;
            const response = await fetch('http://localhost:8009/getAllPublicAIMessages/'+convoid);
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
            const fetchedMessages = await response.json();

            const date1 = fetchedMessages[0].sent;
            const date2 = fetchedMessages[fetchedMessages.length-1].sent;
            this.convodates = `${this.formatDate(date1)} to ${this.formatDate(date2)}`;
            const response2 = await fetch('http://localhost:8010/getFilesForPublicConvo/'+convoid);
            if(!response2.ok) {
                throw new Error('Network response not ok');
            }
            const messageFiles = await response2.json();
            for (let i = 0; i < fetchedMessages.length; i++) {
                    let msg = fetchedMessages[i];
                    let msgFiles = messageFiles.filter(x => x['messageId'] === msg['messageid']);
                    for (let j = 0; j < msgFiles.length; j++) {
                        const byteArray = this.base64ToUint8Array(msgFiles[j]['fileBase64String']);
                        const blob = new Blob([byteArray]);
                        const file = new File([blob], msgFiles[j]['fileName'], { type: msgFiles[j]['fileType'] });
                        msgFiles[j] = file;
                    }

                    if (msg['username'].startsWith("AI to ")) {
                        this.messages.push(["AI", msg['message'], msg['messageid'], msgFiles]);
                    } else {
                        this.messages.push(["user", msg['message'], msg['messageid'], msgFiles]);
                    }
            }
            localStorage.setItem('defaultConvo', convoid);

        }
        catch {
            this.convotitle = "Convo not found";
            this.convodates = "N/A";
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
            if(this.numTimesRouteParamsWatched==0) {
                this.numTimesRouteParamsWatched++;
            }
            else {
                if(typeof newParams.convoid !== 'undefined') {
                    this.convoid = newParams.convoid;
                    this.fetchMessagesForConvo(this.convoid);
                }
                else if(localStorage.getItem('defaultConvo')) {
                
                    this.convoid = localStorage.getItem('defaultConvo');
                    this.fetchMessagesForConvo(this.convoid);
                    }
                else {
                    this.convotitle = "Convo not found";
                    this.convodates = "N/A";
                }
                }
            }
    }
}
}
</script>