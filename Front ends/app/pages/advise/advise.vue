<template>
<view class="uni-padding-wrap">
	<form @submit="commitComments">
		<textarea class="contentTextArea" name="commentContent" v-model="commentContent" placeholder="请留下您的宝贵意见"></textarea>
		<button class="commitButton" formType="submit">立即反馈</button>
	</form>		
</view>
</template>
 
<script>
	export default {
		data() {
			return {
					commentContent:'',
					}
		},
		methods: {
		},
	}
</script>
 
<style>
     .uni-padding-wrap {
		padding: 150upx;
	}
	.commitButton{
		color: black;
		width: 90%;
		margin: 0rpx auto;
		height: 85rpx;
		line-height: 75rpx;
		font-size: 45rpx;
		background: linear-gradient(left top,#86B6FD, #8fc9fc,#86B6FD);
	}
	.contentTextArea{
		font-size: 30rpx;
		height:700rpx;
		border:1rpx solid #e9e7ef;
		width: 85%;
		margin: 0rpx auto;
		margin-bottom: 15rpx;
		padding: 20rpx 0 0 20rpx;
		border-radius: 6rpx;
	}
</style>