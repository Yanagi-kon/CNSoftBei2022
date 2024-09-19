<template>
	<view class="content">
		<block v-for="(item, index) in bookResources" :key="index">
			<view class="flex align-center p-1 border-bottom" hover-class="bg-light" style="height: 320rpx;"
				@touchend="maskTouchend1(item.title,item.id)">
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
	import bookList from '@/components/bookList.vue';
	import searchBox from '@/components/searchBox.vue';
	const recorderManager = uni.getRecorderManager();
	export default {
		data() {
			return {
				stop: true,
				touchNum: 0,
				statusBarHeight: this.$statusBarHeight,
				functionSortArr: [{
						iconId: 'icon-jieshao',
						iconColor: 'text-warning',
						name: '听'
					},
					{
						iconId: 'icon-startRead',
						iconColor: 'text-hover-primary',
						name: '听'
					},
					{
						iconId: 'icon-icon09',
						iconColor: 'text-light-black',
						name: '听'
					},
					{
						iconId: 'icon-zhishi',
						iconColor: 'text-success',
						name: '听'
					}
				],
				swiperImages: [],
				Rebooks: [],
				bookResources: [],
				page: 1,
				size: 10, //每页十条
				Tpage: 0,
				nclass: '',
				innerAudioContext: '',
				btn: false,
				clear: false
			}
		},
		components: {
			bookList,
			searchBox
		},
		onLoad() {
			this.recommend();
			this.innerAudioContext = uni.createInnerAudioContext();
			// this.innerAudioContext.src = this.voicePath;
			// this.innerAudioContext.play();
			
			let self = this;
			recorderManager.onStop(function(res) {
				console.log('recorder stop' + JSON.stringify(res));
				self.voicePath = res.tempFilePath;
				if (uni.getStorageSync("uid") == undefined || uni.getStorageSync("uid") == '') {
					uni.showToast({
						title: '请登录后再试',
						icon: "none"
					});
					return
				}
				uni.uploadFile({
					url: "http://36.138.201.130:9999/api/news/asr?uid=" + uni.getStorageSync(
						"uid"), //仅为示例，非真实的接口地址
					filePath: self.voicePath,
					name: 'file',
					success: function(res) {
						console.log(res)
						debugger
						if (JSON.parse(res.data).data.type == '电量') {
							self.getDevice()
						}
						if (JSON.parse(res.data).data.type == '时间') {
							self.getTime()

						}
						if (JSON.parse(res.data).data.type == '新闻') {
							console.log(JSON.parse(res.data).data.news)
							if (JSON.parse(res.data).data.news.length > 0) {
								self.bookResources = JSON.parse(res.data).data.news
								self.clear = true
							}

						}
						console.log()
						if (JSON.parse(res.data).data.type == '错误') {
							console.log("-----------")
							uni.showToast({
								title: '语音识别错误！',
								icon: "none"
							});
						}

					},
					fail: (err) => {
						console.log('请求error_______________', err)
					}
				});
			});
		},
		onShow() {
		},
		onPullDownRefresh() {
			this.page = 1;
			this.clearM();
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 500);
		},
		onReachBottom() {
			
		},
		methods: {
			maskTouchend() {
				this.touchNum++
				setTimeout(() => {
					if (this.touchNum == 1) {
						this.btnClick('语音指令');
					}
					if (this.touchNum >= 2) {
						this.voice();
					}
					this.touchNum = 0
				}, 250)
			},
			maskTouchend1(title, id) {
				this.touchNum++
				setTimeout(() => {
					if (this.touchNum == 1) {
						this.btnClick(title);
					}
					if (this.touchNum >= 2) {
						this.toNewsDetail(id);
					}
					this.touchNum = 0
				}, 250)
			},
			searchClick(data) {
				this.page = 1;
				this.nclass = data;
				this.searchNews();
			},
			toNewsDetail(id) {
				if (uni.getStorageSync("uid") == '' || uni.getStorageSync("uid") == undefined) {
					uni.showToast({
						title: '登录后查看',
						icon: "none"
					});
					uni.navigateTo({
						url: '../pages/login/login'
					})
					return

				}
				uni.navigateTo({
					url: '/pages/readLIst/readInfo?id=' + id
				})
			},
			clearM() {
				this.$api.getNewsPage({
					page: this.page,
					size: this.size,
					nclass: this.nclass
				}).then((res) => {
					if (this.page == 1) {
						this.bookResources = res.data.data.news;
						this.Tpage = Math.ceil(res.data.data.page.total / this.size)
						this.clear = false
						this.page++;
					} else if (res.data.data.news.length > 0) {
						this.bookResources = this.bookResources.concat(res.data.data.news);
						this.Tpage = Math.ceil(res.data.data.page.total / this.size)
						this.clear = false
						this.page++;
					} else {
						this.stop = false;
					}


				})
			},
			recommend() {
				this.$api.recommend(uni.getStorageSync("uid")?uni.getStorageSync("uid"):6).then((res) => {
					this.bookResources=res.data.data
				})
			},
			searchNews() {
				this.$api.recommend(uni.getStorageSync("uid")?uni.getStorageSync("uid"):6).then((res) => {
					this.bookResources=res.data.data
				})
			},
			voiceStop() {
				uni.showToast({
					title: '请稍后...',
					icon: "none"
				});
				this.btn = false
				recorderManager.stop();
				
			},

			voice() {
				this.btn = true
				uni.showToast({
					title: '请开始说话',
					icon: "none"
				});
				console.log('开始录音');

				recorderManager.start({
					format:"mp3"
				});
			},
			getTime() {
				let month = new Date().getMonth() + 1;
				let day = new Date().getDate();
				let hour = new Date().getHours();
				let minute = new Date().getMinutes();
				let second = new Date().getSeconds();
				this.$api.ttes({
					text: "现在是北京时间" + hour + '时' + minute + '分'
				}).then((res) => {
					if (res.data.code == 200) {
						this.innerAudioContext.autoplay = true;
						this.innerAudioContext.src = res.data.data
						this.innerAudioContext.onPlay(() => {
							console.log('开始播放');
						});
						this.innerAudioContext.onError((res) => {
							console.log(res.errMsg);
							console.log(res.errCode);
						});
					}


					console.log("+++++++" + JSON.stringify(res))
				})


			},
			getDevice() {
				let plat = uni.getSystemInfoSync().platform;
				let _this = this
				if (plat == 'ios') {
					var UIDevice = plus.ios.import('UIDevice')
					var dev = UIDevice.currentDevice()
					if (!dev.isBatteryMonitoringEnabled()) {
						dev.setBatteryMonitoringEnabled(true)
					}
					var level = dev.batteryLevel() * 100 // 获取苹果手机电量  电量要*100
					this.$api.ttes({
						text: "当前电量" + level
					}).then((res) => {
						if (res.data.code == 200) {
							this.innerAudioContext.autoplay = true;
							this.innerAudioContext.src = res.data.data
							this.innerAudioContext.onPlay(() => {
								console.log('开始播放');
							});
							this.innerAudioContext.onError((res) => {
								console.log(res.errMsg);
								console.log(res.errCode);
							});
						}


						console.log("+++++++" + JSON.stringify(res))
					})


				}
				if (plat == 'android') {
					
					var main = plus.android.runtimeMainActivity();
					var Intent = plus.android.importClass('android.content.Intent');  
					var recevier = plus.android.implements('io.dcloud.feature.internal.reflect.BroadcastReceiver', {  
					          onReceive: function(context, intent) {  
					        var action = intent.getAction();  
					        if (action == Intent.ACTION_BATTERY_CHANGED) {  
					            var level   = intent.getIntExtra("level", 0); //电量  
					            var voltage = intent.getIntExtra("voltage", 0); //电池电压  
					            var temperature = intent.getIntExtra("temperature", 0); //电池温度  
					                        //如需获取别的，在这里继续写，此代码只提供获取电量  
					            console.log("当前电量"+level)  
								setTimeout(function() {
									_this.$api.ttes({
										text: "当前电量" + level
									}).then((res) => {
										if (res.data.code == 200) {
											_this.innerAudioContext.autoplay = true;
											_this.innerAudioContext.src = res.data.data
											console.log( res.data.data+"---")
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										}
									
										console.log("+++++++" + JSON.stringify(res))
									})
								}, 200);
								
								
					        }  
					     }  
					 });  
					var IntentFilter = plus.android.importClass('android.content.IntentFilter');  
					var filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);  
					main.registerReceiver(recevier, filter); 
				}
			}

		}
	}
</script>

<style lang="less">
	.clean {
		color: #0056B3;
		font-size: 30rpx;
	}

	.img {
		position: sticky;
		background-color: #fff;
		z-index: 999;

		image {
			width: 250rpx;
			height: 250rpx;
		}

	}

	.recommend {
		margin-left: 30rpx;
		font-weight: bold;
		color: #008080;
	}

	.myvoice {
		margin-right: 10rpx;
		;
	}
</style>
