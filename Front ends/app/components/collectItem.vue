<template>
	<view class="flex align-center p-1 border-bottom" style="height: 250rpx;">
		<image :src="bookImgUrl" mode="aspectFill" class="rounded mx-2" style="width: 220rpx;height: 220rpx;"></image>
		<view class="flex-1 position-relative" style="height: 100%;">
			<view class="py-1 font-md font-weight-bold"> {{bookName}} </view>
			<view class="font text-light-black Multi-row-truncation"> {{bookSynopsis}} </view>
			<my-icon iconId="icon-diandian" class="position-absolute right-0" @my-click="init"></my-icon>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			bookID:{
				type: Number,
				default: 0
			},
			bookImgUrl: {
				type: String,
				default: ''
			},
			bookName: {
				type: String,
				default: ''
			},
			bookSynopsis: {
				type: String,
				default: ''
			},
		},
		methods: {
			init() {
				this.showCancalCol(true);
				this.throwID(this.bookID)
			},
			
			throwID(id){
				this.$emit('getID', id)
			},
			showCancalCol(bol){
				this.$emit('showCancalCol', bol)
			}
		},
	}
</script>

<style>

</style>
