<template>
	<view class="flex align-center justify-center mx-2 border rounded bg-light" style="height: 150rpx; opacity: .8;" @click="btnClick('搜索框')">
	    <image class="myvoice" src="/static/user/voice.png" style="width: 100rpx;height: 100rpx;"></image>
		<input type="text" placeholder="听见全'视'界" placeholder-style="font-size:50rpx;" class="flex-1 font-m" v-model="search" @blur="blurClick()"/>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				search:"",
				innerAudioContext:''
			}
		},
		mounted() {
			this.innerAudioContext = uni.createInnerAudioContext();
		},
		methods: {
			blurClick(){
				console.log(this.search);
				this.$emit('fun',this.search)
			},
			
		},
		mounted() {
			
		}
	}
</script>

<style>
	.myvoice{
		margin-right:10rpx; ;
	}
</style>
