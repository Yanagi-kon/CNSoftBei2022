<template>
	<view class="card-block-wrapper">
		<view class="card-title" v-if="title">{{title}}</view>
		<slot></slot>
	</view>
</template>

<script>
	export default {
		props:{
			title:{
				type:String
			}
		},
		data() {
			return {
				
			};
		}
	}
</script>

<style lang="scss">
.card-block-wrapper{
	    background: #fff;
	    border-radius: 10rpx;
	    box-shadow: 0 0 14px 0 rgba(0,0,0,.08);
	    padding: 20rpx 30rpx 27rpx;
	    overflow: hidden;
		// margin: 30rpx 20rpx;
		.card-title{
			font-size: 30rpx;
			margin-bottom: 15rpx;
		// &:before{
		// 	    content: '';
		// 	    float: left;
		// 	    height: 30rpx;
		// 	    width: 30rpx;
		// 	    margin-right: 13rpx;
		// 	    background: black;
		// 	    border-radius: 50%;
		// }
		}
}
</style>
