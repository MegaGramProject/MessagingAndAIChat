<script setup>
import { useCounterStore } from './stores/counter.js'
import LeftSidebar from './components/LeftSidebar.vue'
import Messages from './components/Messages.vue'
</script>

<template>
  <LeftSidebar/>
</template>

<script>
  export default {
  data() {
      return {
          showLeftSidebar: true
      };
  },
  };
</script>

<style scoped>

</style>
