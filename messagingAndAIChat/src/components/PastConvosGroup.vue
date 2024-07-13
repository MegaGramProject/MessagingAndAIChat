
<script setup>
import PastConvo from './PastConvo.vue';

defineProps({
    timeOfConvoGroup: {
        type: Date,
        required: true
    },
    selectedConvo: {
        type: String,
        required: true
    },
    convoTitles: {
        type: Array,
        required: true
    },
    selectNewConvo: {
        type: Function,
        required: true
    },
    toggleShareChatPopup: {
        type: Function,
        required: true
    }
});
</script>

<template>
<p :style="{fontSize:'0.78em', color:'gray', fontWeight:'500'}">{{formattedDate}}</p>
<template v-for="(title, index) in convoTitles" :key="index">
    <PastConvo :convoTitle="title[1]" :convoId="title[0]" :selectedConvo="selectedConvo" :selectNewConvo="selectNewConvo" :toggleShareChatPopup="toggleShareChatPopup"/>
</template>
<br/>
</template>

<script>
export default {
computed: {
    formattedDate() {
        const today = new Date();
        const givenDate = this.timeOfConvoGroup;

        today.setHours(0, 0, 0, 0);

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
    }
}
};
</script>


