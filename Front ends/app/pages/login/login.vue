<template>

 <view class="uni-padding-wrap">	
     <image class="myface" src="/static/user/face.png" style="width: 600rpx;height: 600rpx;"></image>
	 <button class="Button"  @click="switchToPage()">立即登录/注册</button>	
 </view>
</template>
 
<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			switchToPage() {
				uni.navigateTo({
				
					url: '/pages/login/face'
				});
			}
		},
	}
</script>

<style>

	.uni-padding-wrap {
		position: absolute;
	    height: 100%;
		width: 100%;
		padding-top: 300upx;
		padding-left:90upx;
		background-color: #F0F8FF; //#FFF5EE
	}
	
	.myface{

	}
		
	.Button{
		color: black;
		width:500rpx;
		margin-top :100rpx;
		margin-left :40rpx;
		height: 120rpx;
		line-height: 115rpx;
		font-size: 50rpx;
	}

</style>
