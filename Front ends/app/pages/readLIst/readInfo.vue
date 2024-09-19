<template>
	<view class="box-c news-box">
		<view class="box">
			<text class="box title text-heide-2">{{newsInfo.title}}</text>
		</view>
		<view class="box-c time mt-3">
			<text>{{newsInfo.media}}</text>

		</view>
		<view class="mt-3" v-if="newsInfo.text !=''">
			<view class="" v-for="(item,index) in JSON.parse(newsInfo.text)">
				<view class="" v-if="item.type==1" style="font-size: 36rpx;">{{item.content}}</view>
				<view class="box d-j" v-if="item.type==2">
					<image :src="item.content" mode="aspectFit"></image>
				</view>
			</view>
		</view>
		<view class="box d-ai-c d-jc-c mt-5">
			<view class="line"></view>
			<view class="title-t">你可能感兴趣的新闻</view>
			<view class="line"></view>
		</view>
		<book-list :bookListArr="bookResources"></book-list>

		<!-- <view class="box ">
			<view class="flex-2 image-news">
				<image src="../../static/user/logo1.png" mode="widthFix"></image>
			</view>
			<view class="flex-3">1</view>
		</view> -->
	</view>
</template>

<script>
	import bookList from '@/components/bookList.vue';
	export default {
		data() {
			return {
				id: '',
				newsInfo: {
					text: []
				},
				voice: '',
				innerAudioContext: '',
				uid: uni.getStorageSync("uid"),
				bookResources: [],
				nclass: ''

			}
		},
		components:{
			bookList
		},
		onLoad(option) {
			let _this = this
			this.id = option.id
			setTimeout(function() {
				_this.getInfo()
				_this.getVoice()
			}, 100);


			this.innerAudioContext = uni.createInnerAudioContext();




		},
		methods: {
			getNewsByType() {
				let _this = this
				this.$api.getNewsPage({
					page: 1,
					size: 3,
					nclass: _this.nclass
				}).then((res) => {
					_this.bookResources = res.data.data.news
					_this.$forceUpdate()
				})
			},
			onBackPress(e) {
				this.innerAudioContext.destroy()

			},
			getInfo() {
				let _this = this
				this.$api.getNews({
					nid: this.id,
					uid: this.uid,
					// nid: 978,
					// uid: 2
				}).then((res) => {
					this.newsInfo = res.data.data
					this.nclass = this.newsInfo.nclass
					this.getNewsByType()

				})
			},
			getVoice() {
				this.$api.GetNewsAudio({
					nid: this.id
				}).then((res) => {
					this.voice = res.data.data

					this.innerAudioContext.autoplay = true;
					this.innerAudioContext.src = this.voice
					this.innerAudioContext.onPlay(() => {
						console.log('开始播放');
					});
					this.innerAudioContext.onError((res) => {
						console.log(res.errMsg);
						console.log(res.errCode);
					});
				})
			}


		}
	}
</script>

<style lang="less">
	.image-news {
		image {
			width: 200rpx;
		}
	}

	.title-t {
		font-size: 28rpx;
		color: #868686;
	}

	.line {
		height: 1rpx;
		width: 180rpx;
		background-color: #ababab;
	}

	.news-box {
		padding: 20rpx 20rpx;

		.title {
			font-weight: bold;
			font-size: 32rpx;
		}

		.time {
			text:first-child {
				font-size: 28rpx;
			}

			text:last-child {
				font-size: 24rpx;
				color: #0E151D;
			}
		}

	}
</style>
