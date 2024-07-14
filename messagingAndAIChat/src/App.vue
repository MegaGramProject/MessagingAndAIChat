<script setup>
import LeftSidebar from './components/LeftSidebar.vue';
import Messages from './components/Messages.vue';
import MegagramAIDropdown from './components/MegagramAIDropdown.vue';
import PublicLinkCreated from './components/PublicLinkCreated.vue';
import ShareChat from './components/ShareChat.vue';
import { v4 as uuidv4 } from 'uuid';

</script>

<template>
  <template v-if="showLeftSidebar">
    <div :style="{opacity:backgroundOpacity, pointerEvents:backgroundPointerEvents}">
    <LeftSidebar :toggleLeftSidebar="toggleLeftSidebar" :toggleShareChatPopup="toggleShareChatPopup" :selectedConvo="selectedConvo"
    :selectNewConvo="selectNewConvo" :createNewConvo="createNewConvo" :username="username" :newConvos="newConvos" :uniqueKey="leftSidebarUniqueKey"
    :oldConvoBacktoLife="oldConvoBacktoLife"/>
    </div>
    <div @click="toggleMegagramAIDropdown" class="hoverableElement" :style="{backgroundColor:'white', display:'flex', alignItems:'center', justifyContent:'center', gap:'1em',
      borderRadius:'5px', border:'none', position:'absolute', top:'2%', left:'16%', cursor:'pointer', zIndex:'10'}">
        <span :style="{color:'gray', fontWeight:'semibold', fontSize:'1.4em'}">Megagram AI</span>
        <img :src="dropdownV" :style="{height:'1em', width:'1em'}"/>
    </div>

    <img :src="exitIcon" :style="{height:'2em', width:'2em', position:'absolute', left:'95%', top:'2%', cursor:'pointer'}"/>
  </template>
  <template v-if="!showLeftSidebar">
    <div :style="{display:'flex', position:'absolute', top:'2%', left:'1%', opacity:backgroundOpacity, pointerEvents:backgroundPointerEvents}">
      <img @click="toggleLeftSidebar" @mouseover="toggleOpenSidebarText" @mouseleave="toggleOpenSidebarText" :src="toggleLeftSidebarIcon" :style="{height:'3em', width:'3em', cursor:'pointer'}"/>
      <img @click="createNewConvo()" @mouseover="toggleNewChatText" @mouseleave="toggleNewChatText" :src="newChatIcon2" :style="{height:'3em', width:'3em', cursor:'pointer', marginLeft:'1em'}"/>
    </div>
      <div @click="toggleMegagramAIDropdown" class="hoverableElement" :style="{backgroundColor:'white', display:'flex', alignItems:'center', justifyContent:'center', marginLeft:'2.7em', gap:'1em',
      borderRadius:'5px', border:'none', cursor:'pointer', position:'absolute', left:'7%', top:'2%', zIndex:'10'}">
        <span :style="{color:'gray', fontWeight:'semibold', fontSize:'1.6em'}">Megagram AI</span>
        <img :src="dropdownV" :style="{height:'1em', width:'1em'}"/>
      </div>
    <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'1%', top:'8%', width:'8em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayOpenSidebarText}">Open sidebar</p>
    <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'5.2%', top:'8%', width:'6em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayNewChatText}">New chat</p>

    <img :src="exitIcon" :style="{height:'2em', width:'2em', position:'absolute', left:'95%', top:'2%', cursor:'pointer'}"/>
  </template>
  <template v-if="showShareChatPopup">
  <ShareChat :convoId="shareChatPopupConvoId" :toggleShareChatPopup="toggleShareChatPopup" :togglePublicLinkCreatedPopup="togglePublicLinkCreatedPopup"/>
  </template>

  <template v-if="showPublicLinkCreatedPopup">
  <PublicLinkCreated :convoId="publicLinkCreatedConvoId" :togglePublicLinkCreatedPopup="togglePublicLinkCreatedPopup"/>
  </template>

  <template v-if="showMegagramAIDropdown">
  <MegagramAIDropdown :toggleMegagramAIDropdown="toggleMegagramAIDropdown" :toggleTemporaryChat="toggleTemporaryChat" :isTemporary="temporaryChatMode"/>
  </template>

  <Messages :messages="messages" :setInput="setInput"/>

  <div :style="{position:'absolute', left:'24%', top:'83%', width:'55%', height:'7%', borderStyle:'solid', display:'flex',
  justifyContent:'start', alignItems:'center', backgroundColor: inputDivAndFieldBackground, borderRadius:'2em'}">
  <input type="file" ref="fileInput" @change="handleFileChange" style="display: none;" />
    <template v-if="!temporaryChatMode">
      <img @click="triggerFileInput" :src="attachmentIcon" :style="{height:'3em', width:'3em', objectFit:'contain', cursor:'pointer', marginLeft:'1em'}"/>
    </template>
    <template v-if="temporaryChatMode">
      <img @click="triggerFileInput" :src="whiteAttachmentIcon" :style="{height:'2.3em', width:'2.3em', objectFit:'contain', cursor:'pointer', marginLeft:'1em'}"/>
    </template>
    <textarea ref="inputField" @input="updateCanSendMessage" v-model="inputText" placeholder="Message MegAI" :style="{fontFamily:'Arial', height:'100%', fontSize:'1.25em', padding:'1em 1em',
    outline:'none', border:'none', backgroundColor: inputDivAndFieldBackground, width:'87%', padding:'1em 0.5em', color: inputFieldTextColor}"/>
    <template v-if="!canSendMessage">
      <img :src="sendMessageIcon1" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', objectFit:'contain'}"/>
    </template>
    <template v-if="canSendMessage">
      <template v-if="!temporaryChatMode">
        <img @click="sendMessage()" :src="sendMessageIcon2" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', objectFit:'contain', cursor:'pointer'}"/>
      </template>
      <template v-if="temporaryChatMode">
        <img @click="sendMessage()" :src="whiteSendMessageIcon2" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', objectFit:'contain', cursor:'pointer'}"/>
      </template>
    </template>
  </div>

  <div v-if="temporaryChatMode" :style="{display:'flex', gap:'0.4em', position:'absolute', left:'47%', top:'2.3%', alignItems:'center'}">
    <img :src="temporaryChatIcon" :style="{height:'1.6em', width:'1.6em', objectFit:'contain'}"/>
    <p :style="{fontSize:'1.2em', fontWeight:'semibold'}">Temporary Chat</p>
  </div>
  
</template>

<script>
  import attachmentIcon from '@/assets/images/attachmentIcon.png';
import dropdownV from '@/assets/images/dropdownV.png';
import exitIcon from '@/assets/images/exitIcon.png';
import newChatIcon2 from '@/assets/images/newChatIcon2.png';
import sendMessageIcon1 from '@/assets/images/sendMessageIcon1.png';
import sendMessageIcon2 from '@/assets/images/sendMessageIcon2.png';
import toggleLeftSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import whiteAttachmentIcon from '@/assets/images/whiteAttachmentIcon.png';
import whiteSendMessageIcon2 from '@/assets/images/whiteSendMessageIcon2.png';
import temporaryChatIcon from '@/assets/images/temporaryChatIcon.png';
import '@/assets/styles.css';
  export default {
  data() {
      return {
          showLeftSidebar: false,
          showOpenSidebarText: false,
          showNewChatText: false,
          showShareChatPopup: false,
          shareChatPopupConvoId: "",
          showPublicLinkCreatedPopup: false,
          publicLinkCreatedConvoId: "",
          showMegagramAIDropdown: false,
          temporaryChatMode: false,
          inputText: "",
          canSendMessage: false,
          selectedConvo: '',
          messages: [],
          username: "",
          numberOfTimesRouteParamsWatched: 0,
          newConvos: [],
          leftSidebarUniqueKey: '',
          oldConvoBacktoLife: [],
  }
},
  methods: {
    toggleLeftSidebar() {
      this.showLeftSidebar= !this.showLeftSidebar;
      this.showOpenSidebarText= false;
    },
    toggleOpenSidebarText() {
      this.showOpenSidebarText = !this.showOpenSidebarText;
    },
    toggleNewChatText() {
      this.showNewChatText = !this.showNewChatText;
    },
    toggleShareChatPopup(shareChatPopupConvoId){
      if(this.showShareChatPopup) {
          this.showShareChatPopup = false;
          this.shareChatPopupConvoId = "";
      }
      else {
        this.showShareChatPopup = true;
        this.shareChatPopupConvoId = shareChatPopupConvoId;
      }
    },
    togglePublicLinkCreatedPopup(publicLinkCreatedConvoId) {
      if(this.showPublicLinkCreatedPopup) {
          this.showPublicLinkCreatedPopup = false;
          this.publicLinkCreatedConvoId = "";
      }
      else {
        this.showShareChatPopup = false;
        this.shareChatPopupConvoId = "";
        this.showPublicLinkCreatedPopup = true;
        this.publicLinkCreatedConvoId = publicLinkCreatedConvoId;
      }
    },
    toggleMegagramAIDropdown() {
      this.showMegagramAIDropdown = !this.showMegagramAIDropdown;
    },
    toggleTemporaryChat(){
      this.temporaryChatMode = !this.temporaryChatMode;
      this.showMegagramAIDropdown = false;
      this.messages = [];
    },
    updateCanSendMessage() {
      this.canSendMessage = this.inputText.length>0;
    },
    async selectNewConvo(convoId) {
        if(this.selectedConvo!==convoId) {
            while(this.messages.length>0) {
              this.messages.pop(0);
            }
            this.selectedConvo = convoId;
            const response = await fetch('http://localhost:8006/getAIMessages/'+convoId);
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
            const fetchedMessages = await response.json();
            for(let msg of fetchedMessages) {
                if(msg[0].startsWith("AI to ")) {
                    this.messages.push(["AI", msg[2], msg[1]]);
                }
                else {
                    this.messages.push(["user", msg[2], msg[1]]);
                }
            }
        }
    },
    createNewConvo() {
      if(this.selectedConvo!== "") {
        this.messages = [];
        this.selectedConvo = "";
        this.$refs.inputField.focus();
      }
    },
    async sendMessage() {
      let newMessageId = uuidv4();
      let newAIMessageId = uuidv4();
      this.messages.push(['user', this.inputText, newMessageId]);
      let aiResponse = "this is the AI response to that";
      setTimeout(()=> {
        this.messages.push(['AI', aiResponse, newAIMessageId]);
      }, 1000)

      if(!this.temporaryChatMode) {
        if(this.selectedConvo==="") {
            let newConvoId = uuidv4();
            this.newConvos.unshift([newConvoId, "new convo " + newConvoId, new Date()]);
            this.leftSidebarUniqueKey = newConvoId;
            this.selectedConvo = newConvoId;
            const options = {
              method: "POST",
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                "username": this.username,
                "convoid": newConvoId,
                "convotitle": "new convo " + newConvoId
              })
            };
            const response = await fetch('http://localhost:8008/aiconvo', options);
            if(!response.ok) {
              throw new Error('Network response not ok');
            }
          }
          else {
            this.oldConvoBackToLife = [this.selectedConvo, new Date()];
          }
      options.body = JSON.stringify({
        "message": this.inputText,
        "username": this.username,
        "messageid": newMessageId,
        "convoid": this.selectedConvo,
        "sent": new Date()
      });
      const response2 = await fetch('http://localhost:8008/aimessage', options);
      if(!response2.ok) {
        throw new Error('Network response not ok');
      }
      options.body = JSON.stringify({
        "message": aiResponse,
        "username": "AI to " + this.username,
        "messageid": newAIMessageId,
        "convoid": this.selectedConvo,
        "sent": new Date()
      });
      const response3 = await fetch('http://localhost:8008/aimessage', options);
      if(!response3.ok) {
        throw new Error('Network response not ok');
        }
      }
      this.inputText = "";
    },
    setInput(inputText){
      this.inputText = inputText;
      this.sendMessage();
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        console.log('Selected file:', file.name);
      }
    }
  },
  computed: {
    displayOpenSidebarText() {
      return this.showOpenSidebarText ? 'inline-block' : 'none';
    },
    displayNewChatText() {
      return this.showNewChatText ? 'inline-block' : 'none';
    },
    backgroundPointerEvents() {
      return this.showShareChatPopup  || this.showPublicLinkCreatedPopup || this.temporaryChatMode ? 'none' : 'auto';
    },
    backgroundOpacity() {
      return this.showShareChatPopup || this.showPublicLinkCreatedPopup || this.temporaryChatMode ? '0.25' : '1'
    },
    inputDivAndFieldBackground() {
      return this.temporaryChatMode ? 'black' : '#f7f7f7'
    },
    inputFieldTextColor() {
      return this.temporaryChatMode ? 'white' : 'black';
    }
  },
  watch: {
    '$route.params': {
      immediate: true,
      handler(newParams) {
        if(this.numberOfTimesRouteParamsWatched==0) {
          this.numberOfTimesRouteParamsWatched++;
        }
        else if(typeof newParams.username !== 'undefined') {
          this.username = newParams.username;
          localStorage.setItem('defaultUsername', newParams.username);
        }
        else if(localStorage.getItem('defaultUsername')) {
            this.username = localStorage.getItem('defaultUsername');
        }
      }
    }
  }
  };
</script>

<style scoped>

</style>
