<script setup>
defineProps({
convoTitle: {
    type: String,
    required: true
}
})
</script>

<template>
<div @click="selectConvo" :style="{width:'100%', borderRadius:'7px', cursor:'pointer', display:'flex',
alignItems:'center', justifyContent:'space-between', height:'2.3em', backgroundColor: displayBackgroundColor, position:'relative'}">
<p :style="{fontSize:'0.85em', color:'gray'}">{{convoTitle}}</p>
<img @mouseover="toggleOptionsText" @mouseleave="toggleOptionsText" :src="threeHorizontalDots" :style="{height:'2em', width:'2em'}">
<p :style="{position:'absolute', top:'-40%', left:'75%', backgroundColor:'black', color:'white', width:'5em', borderRadius:'3px',
paddingLeft:'0.6em', fontSize:'0.77em', display: displayOptionsText}">Options</p>
</div>
</template>


<script>
    import threeHorizontalDots from '@/assets/images/threeHorizontalDots.png';
    export default {
    data() {
        return {
            threeHorizontalDots,
            isSelected: false,
            optionsHovered: false
        };
    },
    methods: {
        selectConvo() {
            this.isSelected= true;
        },
        toggleOptionsText() {
            this.optionsHovered = !this.optionsHovered;
        }
    },
    computed: {
        displayBackgroundColor() {
            return this.isSelected ? '#ebebeb' : '';
        },
        displayOptionsText() {
            return this.optionsHovered ? 'inline-block' : 'none';
        }
    }
};
</script>