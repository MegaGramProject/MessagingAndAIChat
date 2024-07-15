<script setup>
import { v4 as uuidv4 } from 'uuid';
import LeftSidebar from './components/LeftSidebar.vue';
import MegagramAIDropdown from './components/MegagramAIDropdown.vue';
import Messages from './components/Messages.vue';
import PublicLinkCreated from './components/PublicLinkCreated.vue';
import ShareChat from './components/ShareChat.vue';


</script>

<template>
  <template v-if="showLeftSidebar">
    <div :style="{opacity:backgroundOpacity, pointerEvents:backgroundPointerEvents}">
    <LeftSidebar :toggleLeftSidebar="toggleLeftSidebar" :toggleShareChatPopup="toggleShareChatPopup" :selectedConvo="selectedConvo"
    :selectNewConvo="selectNewConvo" :createNewConvo="createNewConvo" :username="username" :newConvos="newConvos" :uniqueKey="leftSidebarUniqueKey"
    :oldConvoBackToLife="oldConvoBackToLife"/>
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


  
  <div v-if="fileImages[0]!==null" :style="{width:'48%', backgroundColor: inputDivAndFieldBackground, position:'absolute', top:'80%', left:'28%', height:'6%',
  borderRadius:'2em', display:'flex', alignItems:'center'}">
    <div @mouseover="showFile0" @mouseleave="hideFile0" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[0]" :style="{position:'absolute', top:'0%', left:'0%', width:'80%', height:'80%', objectFit:'contain'}"/>
      <img @click="removeFileInput(0)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName0}">{{ files[0].name }}</p>
    </div>
    <div @mouseover="showFile1" @mouseleave="hideFile1" v-if="fileImages[1]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[1]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(1)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName1}">{{ files[1].name }}</p>
    </div>
    <div @mouseover="showFile2" @mouseleave="hideFile2" v-if="fileImages[2]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[2]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(2)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName2}">{{ files[2].name }}</p>
    </div>
    <div @mouseover="showFile3" @mouseleave="hideFile3" v-if="fileImages[3]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[3]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(3)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName3}">{{ files[3].name }}</p>
    </div>
    <div @mouseover="showFile4" @mouseleave="hideFile4" v-if="fileImages[4]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[4]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(4)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName4}">{{ files[4].name }}</p>
    </div>
    <div @mouseover="showFile5" @mouseleave="hideFile5" v-if="fileImages[5]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[5]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(5)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName5}">{{ files[5].name }}</p>
    </div>
    <div @mouseover="showFile6" @mouseleave="hideFile6" v-if="fileImages[6]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[6]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(6)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName6}">{{ files[6].name }}</p>
    </div>
    <div @mouseover="showFile7" @mouseleave="hideFile7" v-if="fileImages[7]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[7]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(7)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName7}">{{ files[7].name }}</p>
    </div>
    <div @mouseover="showFile8" @mouseleave="hideFile8" v-if="fileImages[8]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[8]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(8)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName8}">{{ files[8].name }}</p>
    </div>
    <div @mouseover="showFile9" @mouseleave="hideFile9" v-if="fileImages[9]!==null" :style="{width:'10%', height:'80%', objectFit:'contain', cursor:'pointer', position:'relative'}">
      <img :src="fileImages[9]" :style="{position:'absolute', top:'0%', left:'0%', width:'100%', height:'100%', objectFit:'contain'}"/>
      <img @click="removeFileInput(9)" :src="refinedXIcon" :style="{position:'absolute', objectFit:'contain', top:'-15%', left:'85%', cursor:'pointer', height:'0.9em', width:'1em'}"/>
      <p :style="{position: 'absolute', color:'white', backgroundColor:'black', top:'-100%', left:'-100%', fontSize:'0.6em', width:'25em', display:displayFileName9}">{{ files[9].name }}</p>
    </div>
  </div>
  <div :style="{position:'absolute', left:'24%', top:'86%', width:'55%', height:'7%', borderStyle:'solid', display:'flex',
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
import fileImage from '@/assets/images/fileImage.png';
import newChatIcon2 from '@/assets/images/newChatIcon2.png';
import refinedXIcon from '@/assets/images/refinedXIcon.png';
import sendMessageIcon1 from '@/assets/images/sendMessageIcon1.png';
import sendMessageIcon2 from '@/assets/images/sendMessageIcon2.png';
import temporaryChatIcon from '@/assets/images/temporaryChatIcon.png';
import toggleLeftSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import whiteAttachmentIcon from '@/assets/images/whiteAttachmentIcon.png';
import whiteSendMessageIcon2 from '@/assets/images/whiteSendMessageIcon2.png';
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
          oldConvoBackToLife: [],
          fileImages: [null, null, null, null, null, null, null, null, null, null],
          availableFileImage: 0,
          files: [],
          showFileName0: false,
          showFileName1: false,
          showFileName2: false,
          showFileName3: false,
          showFileName4: false,
          showFileName5: false,
          showFileName6: false,
          showFileName7: false,
          showFileName8: false,
          showFileName9: false,
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

            const response2 = await fetch('http://localhost:8008/api/getFilesForConvo/'+convoId);
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
  base64ToUint8Array(base64) {
    const binaryString = window.atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
  },
    createNewConvo() {
      if(this.selectedConvo!== "") {
        this.messages = [];
        this.selectedConvo = "";
        this.$refs.inputField.focus();
      }
    },
    async sendMessage() {
      let newConvoId = uuidv4();
      let newMessageId = uuidv4();
      let newAIMessageId = uuidv4();
      this.messages.push(['user', this.inputText, newMessageId, this.files]);
      let aiResponse = "this is the AI response to that";
      setTimeout(()=> {
        this.messages.push(['AI', aiResponse, newAIMessageId, []]);
      }, 1000);
      const options = {
              method: "POST",
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                "username": this.username,
                "convoid": newConvoId,
                "convotitle": "new convo " + newConvoId,
              })
      };

      if(!this.temporaryChatMode) {
        if(this.selectedConvo==="") {
            this.newConvos = [[newConvoId, "new convo " + newConvoId, new Date()], ...this.newConvos];
            this.leftSidebarUniqueKey = newConvoId;
            this.selectedConvo = newConvoId;
            const response = await fetch('http://localhost:8008/api/aiconvo', options);
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
      const response2 = await fetch('http://localhost:8008/api/aimessage', options);
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
      const response3 = await fetch('http://localhost:8008/api/aimessage', options);
      if(!response3.ok) {
        throw new Error('Network response not ok');
        }

      if(this.files.length>0) {
          const formData = new FormData();
          formData.append('messageid', newMessageId);
          this.files.forEach((file, index) => {
            formData.append(`file${index}`, file);
          });

          const response4 = await fetch('http://localhost:8008/api/sendFilesWithMessage', {
            method: 'POST',
            body: formData
          });
          if(!response4.ok) {
            throw new Error('Network response not ok');
          }
          const response4Data = await response4.json();
      }
    }
  
      this.inputText = "";
      this.files = [];
      this.availableFileImage = 0;
      this.fileImages = [null, null, null, null, null, null, null, null, null, null];
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
          const acceptedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/webp', 'image/svg+xml'];
          if (this.availableFileImage<10 && acceptedTypes.includes(file.type)) {
            this.fileImages[this.availableFileImage] = URL.createObjectURL(file);
            this.availableFileImage++;
          }
          else if(this.availableFileImage<10) {
              this.fileImages[this.availableFileImage] = fileImage;
              this.availableFileImage++;
            }
          this.files.push(file);
        }
    },
    removeFileInput(fileNumber) {
      for(let i=fileNumber; i<this.fileImages.length-1; i++) {
        if(this.fileImages[i]==null) {
          break;
        }
        this.fileImages[i] = this.fileImages[i+1];
      }
      this.fileImages[9] = null;
      this.files.splice(fileNumber, 1);
      this.availableFileImage--;
    },
    showFile0() {
      this.showFileName0 = true;
    },
    hideFile0() {
      this.showFileName0 = false;
    },
    showFile1() {
      this.showFileName1 = true;
    },
    hideFile1() {
      this.showFileName1 = false;
    },
    showFile2() {
      this.showFileName2 = true;
    },
    hideFile2() {
      this.showFileName2 = false;
    },
    showFile3() {
      this.showFileName3 = true;
    },
    hideFile3() {
      this.showFileName3 = false;
    },
    showFile4() {
      this.showFileName4 = true;
    },
    hideFile4() {
      this.showFileName4 = false;
    },
    showFile5() {
      this.showFileName5 = true;
    },
    hideFile5() {
      this.showFileName5 = false;
    },
    showFile6() {
      this.showFileName6 = true;
    },
    hideFile6() {
      this.showFileName6 = false;
    },
    showFile7() {
      this.showFileName7 = true;
    },
    hideFile7() {
      this.showFileName7 = false;
    },
    showFile8() {
      this.showFileName8 = true;
    },
    hideFile8() {
      this.showFileName8 = false;
    },
    showFile9() {
      this.showFileName9 = true;
    },
    hideFile9() {
      this.showFileName9 = false;
    },
    
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
    },
    displayFileName0() {
      return this.showFileName0 ? 'inline-block' : 'none';
    },
    displayFileName1() {
      return this.showFileName1 ? 'inline-block' : 'none';
    },
    displayFileName2() {
      return this.showFileName2 ? 'inline-block' : 'none';
    },
    displayFileName3() {
      return this.showFileName3 ? 'inline-block' : 'none';
    },
    displayFileName4() {
      return this.showFileName4 ? 'inline-block' : 'none';
    },
    displayFileName5() {
      return this.showFileName5 ? 'inline-block' : 'none';
    },
    displayFileName6() {
      return this.showFileName6 ? 'inline-block' : 'none';
    },
    displayFileName7() {
      return this.showFileName7 ? 'inline-block' : 'none';
    },
    displayFileName8() {
      return this.showFileName8 ? 'inline-block' : 'none';
    },
    displayFileName9() {
      return this.showFileName9 ? 'inline-block' : 'none';
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
