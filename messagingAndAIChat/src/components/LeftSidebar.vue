<script setup>
import PastConvosGroup from './PastConvosGroup.vue';
defineProps({
toggleLeftSidebar: {
    type: Function,
    required: true
},
toggleShareChatPopup: {
    type: Function,
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
createNewConvo: {
    type: Function,
    required: true
},
username: {
    type: String,
    required: true
},
newConvos: {
    type: Array,
    required: true
},
uniqueKey: {
    type: String,
    required: true
},
oldConvoBackToLife: {
    type: Array,
    required: true
}
})
</script>


<template>
<div :style="{position:'fixed', width:'16em', top:'0%', left:'0%', height:'100%',
display:'flex', flexDirection:'column', backgroundColor:'#f7f7f7', padding: '1em 0.7em', overflowY:'scroll'}">
<div :style="{display:'flex', justifyContent:'space-between'}">
<img @click="toggleLeftSidebar()" @contextmenu.prevent="disableRightClick" :src="toggleSidebarIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em',
objectFit:'contain'}" @mouseover="onSidebarIconHover" @mouseleave="onSidebarIconHover">
<img @click="createNewConvo()" :src="newChatIcon" :style="{cursor:'pointer', height:'2.2em', width:'2.2em', objectFit:'contain'}"
@mouseover="onNewChatIconHover" @mouseleave="onNewChatIconHover">
</div>
<div @contextmenu.prevent="disableRightClick" :style="{display:'flex', justifyContent:'space-between', marginTop:'0.4em'}">
<p :style="{backgroundColor:'black', color:'white', width:'45%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.6em', display: displayShowCloseSidebarText}">Close sidebar</p>
<p :style="{backgroundColor:'black', color:'white', width:'43%', fontSize:'0.77em', borderRadius:'4px', height:'2.37em',
padding: '0.4em 0.4em', marginLeft:'13.5em', display: displayShowNewChatText}">New chat</p>
</div>

<div @click="createNewConvo()" class="hoverableElement" :style="{display:'flex', width:'100%', borderRadius:'8px', height:'3.4em', alignItems:'center', cursor:'pointer'}">
<img :src="chatgptIcon" :style="{height:'2.4em', width:'2.4em', objectFit:'contain'}"/>
<p :style="{marginLeft:'1em'}">MegAI</p>
</div>

<br/>
<br/>
<br/>


<template v-if="newConvosState.length > 0" :key="uniqueKey">
    <PastConvosGroup :timeOfConvoGroup="newConvos[0][2]" :selectedConvo="selectedConvo" :convoTitles="[...newConvosState, ...convosToday]"
    :selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosToday.length>0 && newConvosState.length==0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosToday[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosToday"
    :selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosYesterday.length>0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosYesterday[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosYesterday" :selectNewConvo="selectNewConvo"
    :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosThisWeek.length>0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosThisWeek[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosThisWeek" :selectNewConvo="selectNewConvo"
    :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosThisMonth.length>0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosThisMonth[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosThisMonth" :selectNewConvo="selectNewConvo"
    :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosThisYear.length>0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosThisYear[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosThisYear" :selectNewConvo="selectNewConvo"
    :toggleShareChatPopup="toggleShareChatPopup"/>
</template>

<template v-if="convosBeforeThisYear.length>0">
    <PastConvosGroup :timeOfConvoGroup="new Date(convosBeforeThisYear[0][2])" :selectedConvo="selectedConvo" :convoTitles="convosBeforeThisYear" :selectNewConvo="selectNewConvo"
    :toggleShareChatPopup="toggleShareChatPopup"/>
</template>



</div>
</template>


<script>
import chatgptIcon from '@/assets/images/chatgptIcon.png';
import newChatIcon from '@/assets/images/newChatIcon.png';
import toggleSidebarIcon from '@/assets/images/toggleSidebarIcon.png';
import '@/assets/styles.css';
export default {
data() {
    return {
        toggleSidebarIcon,
        newChatIcon,
        showCloseSidebarText: false,
        showNewChatText: false,
        convosToday: [],
        convosYesterday: [],
        convosThisWeek: [],
        convosThisMonth: [],
        convosThisYear: [],
        convosBeforeThisYear: [],
        newConvosState: []
    };
},

mounted() {
    this.fetchAIConvos(this.username);
},


methods: {
    disableRightClick(event) {
        event.preventDefault();
    },
    onSidebarIconHover() {
        this.showCloseSidebarText = !this.showCloseSidebarText;
    },
    onNewChatIconHover() {
        this.showNewChatText = !this.showNewChatText;
    },
    formattedDate(givenDate) {
        const today = new Date();

        if (givenDate.getTime() >= today.getTime()) {
            return "Today";
        }

        const yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);
        if (givenDate.getTime() === yesterday.getTime()) {
            return "Yesterday";
        }

        const startOfWeek = new Date(today);
        startOfWeek.setDate(today.getDate() - today.getDay());
        if (givenDate >= startOfWeek && givenDate < today) {
            return "This week";
        }

        const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
        if (givenDate >= startOfMonth && givenDate < today) {
            return "This month";
        }

        const startOfYear = new Date(today.getFullYear(), 0, 1);
        if (givenDate >= startOfYear && givenDate < today) {
            return "This year";
        }


    return "Before this year";
    },

    async fetchAIConvos(username){
        const response = await fetch('http://localhost:8006/getAIConvos/'+username);
        if(!response.ok) {
            throw new Error('Network response not ok');
        }
        const aiConvos = await response.json();
        aiConvos.sort((a, b) => new Date(b[2]) - new Date(a[2]));
        for(let convo of aiConvos) {
            const formattedDate = this.formattedDate(new Date(convo[2]));
            if(formattedDate==="Today") {
                this.convosToday.push(convo);
            }
            else if(formattedDate==="Yesterday") {
                this.convosYesterday.push(convo);
            }
            else if(formattedDate==="This week") {
                this.convosThisWeek.push(convo);
            }
            else if(formattedDate==="This month") {
                this.convosThisMonth.push(convo);
            }
            else if(formattedDate==="This year") {
                this.convosThisYear.push(convo);
            }
            else {
                this.convosBeforeThisYear.push(convo);
            }
        }
    }
},

computed: {
    displayShowCloseSidebarText(){
        return this.showCloseSidebarText ? 'inline-block' : 'none';
    },
    displayShowNewChatText(){
        return this.showNewChatText ? 'inline-block' : 'none';
    }
},
watch: {
    newConvos(newVal) {
        this.newConvosState = newVal;
    },

    oldConvoBackToLife(newVal) {
        for(let i=0; i<this.newConvosState.length; i++) {
            if(this.newConvosState[i][0]===newVal[0]) {
                if(i==0) {
                    return;
                }
                convoTitle = this.newConvos[i][1];
                this.newConvosState.splice(i, 1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }

        for(let i=0; i<this.convosToday.length; i++) {
            if(this.convosToday[i][0]===newVal[0]) {
                convoTitle = this.convosToday[i][1];
                this.convosToday.splice(i,1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }
        for(let i=0; i<this.convosThisWeek.length; i++) {
            if(this.convosThisWeek[i][0]===newVal[0]) {
                convoTitle = this.convosThisWeek[i][1];
                this.convosThisWeek.splice(i,1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }
        for(let i=0; i<this.convosThisMonth.length; i++) {
            if(this.convosThisMonth[i][0]===newVal[0]) {
                convoTitle = this.convosThisMonth[i][1];
                this.convosThisMonth.splice(i,1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }
        for(let i=0; i<this.convosThisYear.length; i++) {
            if(this.convosThisYear[i][0]===newVal[0]) {
                convoTitle = this.convosThisYear[i][1];
                this.convosThisYear.splice(i,1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }
        for(let i=0; i<this.convosBeforeThisYear.length; i++) {
            if(this.convosBeforeThisYear[i][0]===newVal[0]) {
                convoTitle = this.convosBeforeThisYear[i][1];
                this.convosBeforeThisYear.splice(i,1);
                this.newConvosState.unshift([newVal[0], convoTitle, newVal[1]]);
                return;
            }
        }
        
    }
}
};
</script>