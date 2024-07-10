
<script setup>
import PastConvo from './PastConvo.vue';

defineProps({
    timeOfConvoGroup: {
        type: Date,
        required: true
    },
    selectedConvo: {
        type: Number,
        required: true
    }
});
</script>

<template>
<p :style="{fontSize:'0.78em', color:'gray', fontWeight:'500'}">{{formattedDate}}</p>
<PastConvo :convoTitle="'Convo3- How to drive a car'" />
<PastConvo :convoTitle="'Convo2- How to horse-ride'" />
<PastConvo :convoTitle="'Convo1- How to time-travel'" />
<br/>
</template>

<script>
export default {
computed: {
    formattedDate() {
        const today = new Date();
        const givenDate = this.timeOfConvoGroup;

        today.setHours(0, 0, 0, 0);
        givenDate.setHours(0, 0, 0, 0);

        if (givenDate.getTime() === today.getTime()) {
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


