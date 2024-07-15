<script setup>
defineProps({
convoTitle: {
    type: String,
    required: true
},
convoId: {
    type: String,
    required: true
},
selectedConvo: {
    type: String,
    required: true
},
selectNewConvo: {
    type: Function,
    required: true
},
toggleShareChatPopup: {
    type: Function,
    required: true
},
})
</script>

<template>
<template v-if="!isDeleted">
<div class="hoverableElement" @click="selectNewConvo(convoId)" :style="{width:'100%', borderRadius:'7px', cursor:'pointer', display:'flex',
alignItems:'center', justifyContent:'space-between', height:'2.3em', backgroundColor: displayBackgroundColor, position:'relative'}">
<template v-if="!isRenaming">
<p :style="{fontSize:'0.85em', color:'gray'}">{{convoTitleState}}</p>
</template>
<template v-if="isRenaming">
<textarea v-model="convoTitleState" :style="{fontFamily:'Arial', width:'100%', height:'70%'}" @keyup.enter="toggleRenaming"></textarea>
</template>
<img @click="toggleOptions" @mouseover="toggleOptionsText" @mouseleave="toggleOptionsText" :src="threeHorizontalDots" :style="{height:'2em', width:'2em'}">
<p :style="{position:'absolute', top:'-40%', left:'75%', backgroundColor:'black', color:'white', width:'5em', borderRadius:'3px',
paddingLeft:'0.6em', fontSize:'0.77em', display: displayOptionsText}">Options</p>
<div :style="{position:'absolute', left:'96%', top:'100%', backgroundColor:'white', borderRadius:'5px',
display:'flex', flexDirection:'column', alignItems:'center', height:'6.4em', padding:'0.35em 0.2em', display: displayOptions,
boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2)', fontSize:'0.8em'}">

<div @click="toggleShareChatPopup(convoId)" class="hoverableElement" :style="{display:'flex', width:'7em', borderRadius:'5px', padding:'0.3em 0.3em'}">
    <img :src="shareConvoIcon" :style="{height:'1.2em', width:'1.2em', objectFit:'contain', marginLeft:'0.6em'}"/>
    <p :style="{marginLeft:'1.3em'}">Share</p>
</div>
<div @click="toggleRenaming" class="hoverableElement" :style="{display:'flex', width:'7em', borderRadius:'5px'}">
    <img :src="renameConvoIcon" :style="{height:'1.2em', width:'1.2em', objectFit:'contain', marginLeft:'0.6em'}"/>
    <p :style="{marginLeft:'1.3em'}">Rename</p>
</div>
<div @click="deleteConvo" class="hoverableElement" :style="{display:'flex', width:'7em', borderRadius:'5px', marginTop:'0.2em'}">
    <img :src="removeConvoIcon" :style="{height:'1.2em', width:'1.2em', objectFit:'contain', marginLeft:'0.6em', marginTop:'0.2em'}"/>
    <p :style="{marginLeft:'1.3em'}">Delete</p>
</div>
</div>
</div>
</template>
</template>


<script>
    import threeHorizontalDots from '@/assets/images/threeHorizontalDots.png';
    import shareConvoIcon from '@/assets/images/shareConvoIcon.png';
    import renameConvoIcon from '@/assets/images/renameConvoIcon.png';
    import removeConvoIcon from '@/assets/images/removeConvoIcon.png';
    import '@/assets/styles.css';
    export default {
    data() {
        return {
            threeHorizontalDots,
            shareConvoIcon,
            optionsHovered: false,
            showOptions: false,
            isRenaming: false,
            convoTitleState: this.convoTitle,
            isDeleted: false
        };
    },
    methods: {
        toggleOptionsText() {
            this.optionsHovered = !this.optionsHovered;
        },
        toggleOptions() {
            this.showOptions = !this.showOptions;
        },
        async toggleRenaming() {
            if(this.isRenaming) {
                const options = {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        "convotitle": this.convoTitleState
                    })
                };
                const response = await fetch('http://localhost:8008/api/aiconvo/'+this.convoId, options);
                if(!response.ok) {
                    throw new Error('Network response not ok');
                }
                this.isRenaming = false;
            }
            else {
                this.isRenaming = true;
            }
        },
        async deleteConvo() {
            const options = {
                method: 'DELETE'
            }
            const response = await fetch('http://localhost:8008/api/aiconvo/'+this.convoId, options);
            if(!response.ok) {
                throw new Error('Network response not ok');
            }
            this.isDeleted = true;

        }
    },
    computed: {
        displayBackgroundColor() {
            return this.convoId == this.selectedConvo ? '#ebebeb' : '';
        },
        displayOptionsText() {
            return this.optionsHovered ? 'inline-block' : 'none';
        },
        displayOptions() {
            return this.showOptions ? 'inline-block' : 'none';
        }
    }
};
</script>