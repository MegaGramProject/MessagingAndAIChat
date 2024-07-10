<script setup>
defineProps({
convoId: {
    type: Number,
    required: true
}
})
</script>

<template>
<div :style="{backgroundColor:'white'}">
<p>Share chat for convo ID: {{ convoId }}</p>
</div>

</template>