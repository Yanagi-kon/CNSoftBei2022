<template>
	<view class="flex align-center border-bottom" style="height: 80rpx;justify-content: space-around;">
		<block v-for="(item, index) in tabArr" :key="index">
			<view class="flex flex-column align-center justify-between" style="height: 80rpx;" @tap="changeTabIndex(index)">
				<view class="text-light-black pt-1 animated faster" :class="tabIndex === index ? 'font-weight-bold pulse':''">
					{{item}} </view>
				<view class="rounded animated zoomIn faster" style="width: 80rpx;height: 8rpx;background-color: #fb5f39;" v-if="tabIndex === index"></view>
			</view>
		</block>
	</view>
</template>

<script>
	export default {
		props: {
			tabIndex: {
				type: Number,
				default: 0
			},
			tabArr: {
				type: Array,
				default: () => []
			},
		},
		methods: {
			changeTabIndex(index) {
				this.$emit('changeTabIndex', index)
			}
		},
	}
</script>

<style>

</style>
