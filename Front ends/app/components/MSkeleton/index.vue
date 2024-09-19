<template>
	<view class="skeleton-wrapper" :style="{
		width:loading?width:'auto',
	    display:!isBlock?'inline-block':'block'}"
		>
		<view class="loading-skeleton" :style="{width,height,left:left+'rpx'}" v-if="loading">
			<view class="animate-bar"></view>
		</view>
		<slot v-else></slot>
	</view>
</template>

<script>
/**
 * Maybe 骨架屏组件
 * */
export default {
	name: 'Skeleton',
	props: {
		loading: {
			type: Boolean,
			default: true
		},
		width: {
			type: String,
			default: '300rpx'
		},
		height: {
			type: String,
			default: '30rpx'
		},
		isBlock:{
			type: Boolean,
			default: false
		},
		left:{
			type: Number,
			default: 0
		}
	}
};
</script>

<style lang="scss" scoped>
.skeleton-wrapper {
	/* display: inline-block; */
}
.loading-skeleton {
	background-color: #C4D1D7;
	background-repeat: no-repeat;
	display: inline-block;
	position: relative;
	overflow: hidden;
	.animate-bar{
		position: absolute;
		height: 100%;
		width: 100%;
	    animation: progress 1.2s ease-in-out infinite;
		background-color: rgba(#ffffff,0.1);
	}
}

@keyframes progress {
	0% {
		transform: translateX(-100%);
	}
	100% {
		transform: translateX(0%);
	}
}
</style>
