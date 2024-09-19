<template>
	<view :class="Theme">
		<!-- #ifndef MP-WEIXIN -->
		<view :style="{height:`${statusBarHeight}px`, paddingTop:`60rpx`}" :class="Theme"></view>
		<view :style="{height:`${statusBarHeight}px`}" class="fixed-top" :class="Theme"></view>
		<view class="flex align-center position-fixed w-100" :class="Theme" :style="{height:`60rpx`,top:`${statusBarHeight}px`}" style="z-index: 99;">
			<my-icon iconId="icon-jiantou-copy" class="mx-2" @my-click="quit"></my-icon>
			<view class="font-lg">
				<slot></slot>
			</view>
		</view>
		<!-- #endif -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				statusBarHeight: this.$statusBarHeight
			};
		},
		props: {
			Theme: {
				type: String,
				default: 'bg-white'
			},
		},
		methods: {
			quit() {
				uni.navigateBack({
					delta: 1
				});
			}
		},
	}
</script>

<style>

</style>
