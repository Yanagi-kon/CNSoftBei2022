<template>
	<view>
		<view :style="{height: `${statusBarHeight}px`}"></view>

		<view class="flex align-center justify-between px-2" style="height: 200rpx;">
			<!-- 用户 -->
			<view class="flex align-center" @tap="open">
				<image src="/static/user/girl.png" mode="aspectFill"
					style="width: 130rpx;height: 130rpx;border-radius: 100%;"></image>
				<text class="pl-2">{{user.nickname}}</text>
			</view>
			<!-- 客服 -->
			<view class="flex align-center rounded-circle p-1" style="background-color: #ecf0f3;">
				<my-icon iconId="icon-erji" iconSize="50" class="px-1"></my-icon>
				<text class="font">我的客服</text>
			</view>

		</view>
		<view style="height:3rpx;background-color: #d1d1d1;"></view>

		<block v-for="(item, index) in ['个人信息','意见反馈','常见问题','退出登录']" :key="index">
			<view>
				<view class="flex align-center justify-between px-5 text-light-black" @touchend="maskTouchend(index)"
					style="height: 150rpx;">
					<text class="my">{{item}}</text>
					<my-icon iconId="icon-iconfonti"></my-icon>
				</view>
				<view style="height: 12rpx;background-color: #F8F8F8;"></view>
			</view>
		</block>

	</view>
</template>

<script>
	let pageList = {
		[0]: "/pages/info/info",
		[1]: "/pages/advise/advise",
		[2]: " ",
		[3]: " ",
	};

	export default {
		data() {
			return {
				statusBarHeight: this.$statusBarHeight,
				user: '',
				touchNum: 0
			}
		},
		onShow() {
			this.getUserInfo()
			
			this.innerAudioContext = uni.createInnerAudioContext();
			this.innerAudioContext.autoplay = true;
			this.innerAudioContext.src = '../../static/grxx.mp3';// res.data.data
			this.innerAudioContext.onPlay(() => {
							console.log('开始播放');
						}); 
			this.innerAudioContext.onError((res) => {
							console.log(res.errMsg);
							console.log(res.errCode);
						});

		},
		methods: {
			getUserInfo() {
				let uid = uni.getStorageSync('uid')
				if (uid == '') {
					uni.navigateTo({
						url: '../login/login'
					})

				} else {
					this.$api.getUser({
						uid: uid
					}).then((res) => {
						console.log(res)
						this.user = res.data.data

					})
				}

			},
			open() {
				uni.navigateTo({
					url: '../info/info'
				})
			},
			maskTouchend(index) {
				if (index == 3) {
					this.touchNum++
					setTimeout(() => {
						if (this.touchNum == 1) {
							this.btnClick('退出登录');
						}
						if (this.touchNum >= 2) {
							this.switchToPage(index);
						}
						this.touchNum = 0
					}, 250)
				} else {
					this.switchToPage(index);
				}

			},
			switchToPage(pageIndex) {
				if (pageIndex == 0) {
					uni.navigateTo({
						url: '../info/info'
					})
				}
				if (pageIndex == 1) {
					uni.navigateTo({
						url: '../advise/advise'
					})
				}

				if (pageIndex == 3) {
					uni.setStorageSync("uid", "")
					uni.navigateTo({
						url: '../login/login'
					})
				}
			}
		},
	}
</script>

<style>
	.my{
		font-size:60rpx;
	}
</style>
