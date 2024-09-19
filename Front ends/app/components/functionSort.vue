<template>
	<view class="flex align-center justify-between mx-3 my-4">
		<block v-for="(item,index) in functionSortArr" :key="index">
			<view class="flex flex-column align-center" @tap="switchToPage(index)">
				<view style="height: 50rpx;background-color: #F8F8F8;"></view>
				<my-icon :iconId="item.iconId" :iconColor="item.iconColor" iconSize="150"></my-icon>
				<text class="mt-1 font text-light-black">{{item.name}}</text>
				<view style="height: 50rpx;background-color: #F8F8F8;"></view>
			</view>
		</block>
	</view>
</template>

<script>
	let pageList = {
		[0]: "/pages/readLIst/readLIst",
		[1]: "/pages/listenNoval/listenNoval",
		[2]: "/pages/musicDetail/musicDetail",
		[3]: "/pages/weather/weather",
	};
	export default {
		props: {
			functionSortArr: {
				type: Array,
				default: () => []
			},
		},
		methods: {
			switchToPage(pageIndex) {
				uni.navigateTo({
					url: pageList[pageIndex]
				});
			}
		},
	}
</script>

<style>

</style>
