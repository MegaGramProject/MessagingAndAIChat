<script setup>
//import { useCounterStore } from './stores/counter.js';
import LeftSidebar from './components/LeftSidebar.vue';
//import Messages from './components/Messages.vue';
import ShareChat from './components/ShareChat.vue';
</script>

<template>
  <template v-if="showLeftSidebar">
    <LeftSidebar :toggleLeftSidebar="toggleLeftSidebar" :toggleShareChatPopup="toggleShareChatPopup"/>
  </template>
  <template v-if="!showLeftSidebar">
    <div :style="{display:'flex', position:'absolute', top:'2%', left:'1%'}">
      <img @click="toggleLeftSidebar" @mouseover="toggleOpenSidebarText" @mouseleave="toggleOpenSidebarText" :src="toggleLeftSidebarIcon" :style="{height:'3em', width:'3em', cursor:'pointer'}"/>
      <img @mouseover="toggleNewChatText" @mouseleave="toggleNewChatText" :src="newChatIcon2" :style="{height:'3em', width:'3em', cursor:'pointer', marginLeft:'1em'}"/>
      <p :style="{color:'gray', fontWeight:'semibold', fontSize:'1.7em', marginLeft:'1.4em'}">Megagram AI</p>
      <div :style="{position:'relative'}">
        <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'-19em', top:'4.5em', width:'8em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayOpenSidebarText}">Open sidebar</p>
        <p :style="{color:'white', backgroundColor:'black', position:'absolute', left:'-15em', top:'4.5em', width:'6em',
        borderRadius:'5px', paddingLeft:'0.6em', display: displayNewChatText}">New chat</p>
      </div>
    </div>
  </template>
  <template v-if="showShareChatPopup">
  <ShareChat :convoId="shareChatPopupConvoId"/>
  </template>
</template>

<script>
  import newChatIcon2 from '@/assets/images/newChatIcon2.png';
import toggleLeftSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
  export default {
  data() {
      return {
          showLeftSidebar: false,
          toggleLeftSidebarIcon,
          showOpenSidebarText: false,
          showNewChatText: false,
          showShareChatPopup: false,
          shareChatPopupConvoId: ""
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
    }

  },
  computed: {
    displayOpenSidebarText() {
      return this.showOpenSidebarText ? 'inline-block' : 'none';
    },
    displayNewChatText() {
      return this.showNewChatText ? 'inline-block' : 'none';
    }
  }
  };
</script>

<style scoped>

</style>
