<template>
	<view>
		<block v-for="(item, index) in bookListArr" :key="index">
			<view class="flex align-center p-1 border-bottom" hover-class="bg-light" style="height: 320rpx;"
				@tap="toNewsDetail(item.id)">
				<image :src="item.image" mode="aspectFill" class="rounded mx-2"
					style="width: 250rpx;height: 250rpx;z-index: 1;"></image>
				<view class="flex-1" style="height: 100%;">
					<view class="py-2 font-md font-weight-bold"> {{item.title}} </view>
					<view class="content box ">
						<text class="text-heide-4">
							<text v-for="(item,index) in JSON.parse(item.text)">
								<text v-if="item.type==1">{{item.content}}</text>
							</text>
					
						</text>
					</view>
				</view>

			</view>
		</block>
	</view>
</template>

<script>
	export default {
		props: {
			bookListArr: {
				type: Array,
				default: () => []
			},
		},
		methods: {
			toNewsDetail(id) {
				if(uni.getStorageSync("uid") =='' || uni.getStorageSync("uid")==undefined){
					uni.showToast({
						title: '登录后查看',
						icon: "none"
					});
					uni.navigateTo({
						url:'../pages/login/login'
					})
					return
					
				}
				uni.navigateTo({
					url:'/pages/readLIst/readInfo?id='+id
				})
			}
		},
	}
</script>

<style lang="less">
	.content{
		text{
			color: #0056B3;
			font-size: 28rpx;
		}
		
	}

</style>
