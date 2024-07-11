<script setup>
//import { useCounterStore } from './stores/counter.js';
import LeftSidebar from './components/LeftSidebar.vue';
//import Messages from './components/Messages.vue';
import PublicLinkCreated from './components/PublicLinkCreated.vue';
import ShareChat from './components/ShareChat.vue';
import MegagramAIDropdown from './components/MegagramAIDropdown.vue';
</script>

<template>
  <template v-if="showLeftSidebar">
    <div :style="{opacity:backgroundOpacity, pointerEvents:backgroundPointerEvents}">
    <LeftSidebar :toggleLeftSidebar="toggleLeftSidebar" :toggleShareChatPopup="toggleShareChatPopup"/>
    <div @click="toggleMegagramAIDropdown" class="hoverableElement" :style="{backgroundColor:'white', display:'flex', alignItems:'center', justifyContent:'center', gap:'1em',
      borderRadius:'5px', border:'none', position:'absolute', top:'2%', left:'19%', cursor:'pointer'}">
        <span :style="{color:'gray', fontWeight:'semibold', fontSize:'1.4em'}">Megagram AI</span>
        <img :src="dropdownV" :style="{height:'1em', width:'1em'}"/>
    </div>
    </div>
    <img :src="exitIcon" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', top:'1%', cursor:'pointer'}"/>
  </template>
  <template v-if="!showLeftSidebar">
    <div :style="{display:'flex', position:'absolute', top:'2%', left:'1%', opacity:backgroundOpacity, pointerEvents:backgroundPointerEvents}">
      <img @click="toggleLeftSidebar" @mouseover="toggleOpenSidebarText" @mouseleave="toggleOpenSidebarText" :src="toggleLeftSidebarIcon" :style="{height:'3em', width:'3em', cursor:'pointer'}"/>
      <img @mouseover="toggleNewChatText" @mouseleave="toggleNewChatText" :src="newChatIcon2" :style="{height:'3em', width:'3em', cursor:'pointer', marginLeft:'1em'}"/>
      <div @click="toggleMegagramAIDropdown" class="hoverableElement" :style="{backgroundColor:'white', display:'flex', alignItems:'center', justifyContent:'center', marginLeft:'2.7em', gap:'1em',
      borderRadius:'5px', border:'none', cursor:'pointer'}">
        <span :style="{color:'gray', fontWeight:'semibold', fontSize:'1.6em'}">Megagram AI</span>
        <img :src="dropdownV" :style="{height:'1em', width:'1em'}"/>
      </div>
      <div :style="{position:'relative'}">
        <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'-19em', top:'4.5em', width:'8em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayOpenSidebarText}">Open sidebar</p>
        <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'-15em', top:'4.5em', width:'6em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayNewChatText}">New chat</p>
      </div>
    </div>
    <img :src="exitIcon" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', top:'1%', cursor:'pointer'}"/>
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

  <div :style="{position:'absolute', left:'20%', top:'83%', width:'60%', height:'7%', borderStyle:'solid', display:'flex',
  justifyContent:'start', alignItems:'center', backgroundColor:'#f7f7f7', borderRadius:'2em'}">
    <img :src="attachmentIcon" :style="{height:'3em', width:'3em', objectFit:'contain', cursor:'pointer', marginLeft:'1em'}"/>
    <textarea @input="updateCanSendMessage" v-model="inputText" placeholder="Message MegAI" :style="{fontFamily:'Arial', height:'100%', fontSize:'1.5em', padding:'1em 1em',
    outline:'none', border:'none', backgroundColor:'#f7f7f7', width:'87%', padding:'0.6em 0.3em'}"/>
    <template v-if="!canSendMessage">
      <img :src="sendMessageIcon1" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', objectFit:'contain'}"/>
    </template>
    <template v-if="canSendMessage">
      <img :src="sendMessageIcon2" :style="{height:'3em', width:'3em', position:'absolute', left:'95%', objectFit:'contain', cursor:'pointer'}"/>
    </template>
  </div>
  
</template>

<script>
  import dropdownV from '@/assets/images/dropdownV.png';
import exitIcon from '@/assets/images/exitIcon.png';
import newChatIcon2 from '@/assets/images/newChatIcon2.png';
import toggleLeftSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import attachmentIcon from '@/assets/images/attachmentIcon.png';
import sendMessageIcon1 from '@/assets/images/sendMessageIcon1.png';
import sendMessageIcon2 from '@/assets/images/sendMessageIcon2.png';
import '@/assets/styles.css';
  export default {
  data() {
      return {
          showLeftSidebar: false,
          toggleLeftSidebarIcon,
          showOpenSidebarText: false,
          showNewChatText: false,
          showShareChatPopup: false,
          shareChatPopupConvoId: "",
          showPublicLinkCreatedPopup: false,
          publicLinkCreatedConvoId: "",
          showMegagramAIDropdown: false,
          temporaryChatMode: false,
          inputText: "",
          canSendMessage: false
      };
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
    },
    updateCanSendMessage() {
      this.canSendMessage = this.inputText.length>0;
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
  }

  };
</script>

<style scoped>

</style>
