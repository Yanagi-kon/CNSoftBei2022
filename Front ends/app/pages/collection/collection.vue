<template>
	<view>
		<view class="cal" :style="{height: `${statusBarHeight}px`}"></view>
		<search-box class="cal"></search-box>
		<tab-top class="cal" :tabIndex="tabIndex" :tabArr="['拍照阅读','场景识别翻译', '历史记录']" @changeTabIndex="changeTabIndex">
		</tab-top>
		<swiper :style="{height: `${calHeight}rpx`}" :current="tabIndex" @change="swiperChangeIndex">

			<!-- 拍照阅读 -->
			<swiper-item>
				<scroll-view scroll-y :style="{height: `${calHeight}rpx`}">
					<!-- 最好的方案是绑定id(特别是涉及到增加和删除数据的时候) -->
					<block v-for="item in collectedArr" :key="item.id">
						<collect-item :bookID="item.id" :bookImgUrl="item.imgurl" :bookName="item.name"
							:bookSynopsis="item.synopsis" @getID="getID" @showCancalCol="showCancalCol"></collect-item>
					</block>
					<view class="mycamera">
						<image src="/static/user/camera.png" mode="aspectFill"
							style="width:500rpx;height:500rpx;border-radius: 100%;"></image>
					</view>
					<button class="mytext1"  @touchend="maskTouchend(1)" >拍摄照片</button>
					<button class="mytext2"  @touchend="maskTouchend(2)" padding="20rpx">从相册选择</button>
				</scroll-view>
			</swiper-item>
			<!-- 场景/物体识别 -->
			<swiper-item>
				<scroll-view scroll-y :style="{height: `${calHeight}rpx`}">
					<!-- 最好的方案是绑定id(特别是涉及到增加和删除数据的时候) -->
					<block v-for="item in collectedArr" :key="item.id">
						<collect-item :bookID="item.id" :bookImgUrl="item.imgurl" :bookName="item.name"
							:bookSynopsis="item.synopsis" @getID="getID" @showCancalCol="showCancalCol"></collect-item>
					</block>
					<view class="mycamera">
						<image src="/static/user/camera.png" mode="aspectFill"
							style="width:500rpx;height:500rpx;border-radius: 100%;"></image>
					</view>
					<button class="mytext1"  @touchend="maskTouchend(3)" >拍摄照片</button>
					<button class="mytext2"  @touchend="maskTouchend(4)" >从相册选择</button>
				</scroll-view>
			</swiper-item>

			<!-- 历史记录-->
			<swiper-item>
				<scroll-view scroll-y :style="{height: `${calHeight}rpx`}">
					<view class="box-c history mt-2 px-5" v-for="(item,index) in historyList">
						<view class="">{{item.typeName}}</view>
						<view class="text-heide-2 title" v-if="item.type==1 || item.type==3">{{item.detail}}</view>
						<view class="mt-2 time">{{item.createTime}}</view>
						<view class="line mt-1"></view>

					</view>
				</scroll-view>
			</swiper-item>
		</swiper>
		<view class="content" v-if="show">
			<view class="box d-jc-end" style="color: #515151;">
				<text @tap='close()'>x</text>
			</view>
			<view class="">
				<scroll-view scroll-y="true" class="scroll-Y" style="height: 550rpx;" @scrolltolower="lower"
					@scroll="scroll">
					{{content}}
				</scroll-view>

			</view>
		</view>

	</view>
</template>

<script>
	import $U from '@/common/unit';
	import tabTop from '@/components/tabTop.vue';
	import searchBox from '@/components/searchBox.vue';
	import collectItem from '@/components/collectItem.vue';
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import {
		pathToBase64,
		base64ToPath
	} from 'image-tools'
	export default {
		data() {
			return {
				statusBarHeight: this.$statusBarHeight,
				calHeight: 0,
				tabIndex: 0,
				showCancalColStatus: false,
				collectedArr: [],
				cancalColID: 0,
				touchNum: 0,
				show: false,
				content: '',
				historyList: []

			}
		},
		components: {
			tabTop,
			searchBox,
			collectItem,
			uniPopup
		},
		
		onShow(){
			this.innerAudioContext = uni.createInnerAudioContext();
			this.innerAudioContext.autoplay = true;
			this.innerAudioContext.src = '../../static/znsb.mp3';// res.data.data
			this.innerAudioContext.onPlay(() => {
							console.log('开始播放');
						});
			this.innerAudioContext.onError((res) => {
							console.log(res.errMsg);
							console.log(res.errCode);
						});
		},
		
		watch: {
			// 监听tabbar的展示
			showCancalColStatus(newVal) {
				newVal ? uni.hideTabBar({
					animation: true
				}) : uni.showTabBar({
					animation: true
				})
			}
		},
		methods: {
			maskTouchend(num) {
				this.touchNum++
				setTimeout(() => {
					if (this.touchNum == 1) {
						if(num==1){
							 //this.btnClick('拍摄文字照片');
							this.innerAudioContext = uni.createInnerAudioContext();
							this.innerAudioContext.autoplay = true;
							this.innerAudioContext.src = '../../static/wzzp.mp3';// res.data.data
							this.innerAudioContext.onPlay(() => {
											console.log('开始播放');
										});
							this.innerAudioContext.onError((res) => {
											console.log(res.errMsg);
											console.log(res.errCode);
										});
							
						}
						   
					    if(num==2){
							//this.btnClick('从相册选择');
							this.innerAudioContext = uni.createInnerAudioContext();
							this.innerAudioContext.autoplay = true;
							this.innerAudioContext.src = '../../static/xiangce.mp3';// res.data.data
							this.innerAudioContext.onPlay(() => {
											console.log('开始播放');
										});
							this.innerAudioContext.onError((res) => {
											console.log(res.errMsg);
											console.log(res.errCode);
										});
						}  
						    
						if(num==3){
						     // this.btnClick('拍摄场景照片');
							this.innerAudioContext = uni.createInnerAudioContext();
							this.innerAudioContext.autoplay = true;
							this.innerAudioContext.src = '../../static/cjzp.mp3';// res.data.data
							this.innerAudioContext.onPlay(() => {
											console.log('开始播放');
										});
							this.innerAudioContext.onError((res) => {
											console.log(res.errMsg);
											console.log(res.errCode);
										}); 
							 
						}
						if(num==4){
						    //this.btnClick('从相册选择');
							this.innerAudioContext = uni.createInnerAudioContext();
							this.innerAudioContext.autoplay = true;
							this.innerAudioContext.src = '../../static/从相册选择.mp3';// res.data.data
							this.innerAudioContext.onPlay(() => {
											console.log('开始播放');
										});
							this.innerAudioContext.onError((res) => {
											console.log(res.errMsg);
											console.log(res.errCode);
										}); 
							  
						}
					}
					if (this.touchNum >= 2) {
						//this.chooseImage(num);
						if(num==1)
						   	this.choose1(num);
						if(num==2)
						    this.choose2(num);
						if(num==3)
						    this.choose1(num);
						if(num==4)
						    this.choose2(num);
					}
					this.touchNum = 0
				}, 250)
			},
			history() {
				if (uni.getStorageSync("uid") == '' || uni.getStorageSync("uid") == undefined) {
					uni.navigateTo({
						url: '../login/login'
					})
					return
				}
				this.$api.history({
					page: 1,
					size: 20,
					uid: uni.getStorageSync("uid")
				}).then((res) => {
					this.historyList = res.data.data.news
				})
			},
			close() {
				this.show = false
				this.innerAudioContext.stop()
			},
			//图片转base64
			getImageBase64(file, suc) {

				uni.getFileSystemManager().readFile({
					filePath: file, //选择图片返回的相对路径
					encoding: 'base64', //编码格式
					success: (res) => { //成功的回调

						let data = res.data
						let base64 = 'data:image/jpeg;base64,' + data //不加上这串字符，在页面无法显示的哦
						suc(base64)
						console.log(base64);
					}
				})
			},
			
			detailImage(path, callback) {
				// #ifdef APP-PLUS
				plus.io.resolveLocalFileSystemURL(path, function(entry) {
					entry.file(function(file) {
						var fileReader = new plus.io.FileReader();
						//alert("getFile:" + JSON.stringify(file));
						fileReader.readAsDataURL(file);
						fileReader.onloadend = function(evt) {
							// console.log(JSON.stringify(evt.target.result)); base64字符串  
							callback(evt.target.result);
						}
					})
				})
				// #endif
			 
			 
				// #ifdef H5
				var img = new Image();
				img.src = path;
				img.onload = function() {
					//默认按比例压缩
					var w = this.width,
						h = this.height;
					var quality = 0.95; // 默认图片质量为0.7
			 
					//生成canvas
					var canvas = document.createElement('canvas');
					var ctx = canvas.getContext('2d');
			 
					// 创建属性节点
					canvas.setAttribute("width", w);
					canvas.setAttribute("height", h);
			 
					ctx.drawImage(this, 0, 0, w, h);
					// quality值越小，所绘制出的图像越模糊
					var base64 = canvas.toDataURL('image/jpeg', quality);
					// 回调函数返回base64的值
					callback(base64);
				};
				// #endif
			},
			
			choose1 (num) {
				let _this = this
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
				//	sourceType: ['camera', 'album'], //这要注意，camera掉拍照，album是打开手机相册
				    sourceType: ['camera'],
					success: (res) => {
						// uni.setStorageSync('uid', '1002')
						if (uni.getStorageSync("uid") == undefined || uni.getStorageSync("uid") == '') {
							uni.showToast({
								title: '请登录后再试',
								icon: "none"
							});
							return
						}
			
						if (num == 1||num==2) {
							uni.uploadFile({
								url: "http://36.138.201.130:9999/api/general/textRec?uid=" + uni
									.getStorageSync("uid"), //仅为示例，非真实的接口地址
								filePath: res.tempFilePaths[0],
								name: 'file',
								header: {
									ContentType: "image/jpg"
								},
								success: function(res) {
									let a = JSON.parse(res.data);
									console.log(a, '文字识别结果');
									if (a.code == 200) {
										_this.show = true
										_this.content = a.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									}
			
								},
								fail: (err) => {
									console.log('请求error_______________', err)
								},
							});
						} else {
							this.detailImage( res.tempFilePaths[0], (data)=>{
								var image=data.replace(/^data:image\/\w+;base64,/, "");
							uni.request({
								url: "http://36.138.201.130:9999/api/general/caption?image=" +image, //服务器端地址itemRec
								data: {
							 
								},
								 
								header: {
									 "Content-Type": "application/json"
								},
								method: 'POST',
								success: (res) => {
									console.log(res);
									if (res.data.code == 200) {
										// uni.showModal({
										// 	title: '识别成功',
										// 	content: res.data.data 
										// })
										_this.show = true
										_this.content = res.data.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									} else {
										uni.showModal({
											title: '提示',
											content: res.data.msg
										})
									}
								}
							});
							})
						}
			
			
					}
				})
			},
			
			choose2 (num) {
				let _this = this
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
				//	sourceType: ['camera', 'album'], //这要注意，camera掉拍照，album是打开手机相册
				    sourceType: ['album'],
					success: (res) => {
						// uni.setStorageSync('uid', '1002')
						if (uni.getStorageSync("uid") == undefined || uni.getStorageSync("uid") == '') {
							uni.showToast({
								title: '请登录后再试',
								icon: "none"
							});
							return
						}
			
						if (num == 1||num==2) {
							uni.uploadFile({
								url: "http://36.138.201.130:9999/api/general/textRec?uid=" + uni
									.getStorageSync("uid"), //仅为示例，非真实的接口地址
								filePath: res.tempFilePaths[0],
								name: 'file',
								header: {
									ContentType: "image/jpg"
								},
								success: function(res) {
									let a = JSON.parse(res.data);
									console.log(a, '文字识别结果');
									if (a.code == 200) {
										_this.show = true
										_this.content = a.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									}
			
								},
								fail: (err) => {
									console.log('请求error_______________', err)
								},
							});
						} else {
							this.detailImage( res.tempFilePaths[0], (data)=>{
								var image=data.replace(/^data:image\/\w+;base64,/, "");
							uni.request({
								url: "http://36.138.201.130:9999/api/general/caption?image=" +image, //服务器端地址itemRec
								data: {
							 
								},
								 
								header: {
									 "Content-Type": "application/json"
								},
								method: 'POST',
								success: (res) => {
									console.log(res);
									if (res.data.code == 200) {
										// uni.showModal({
										// 	title: '识别成功',
										// 	content: res.data.data 
										// })
										_this.show = true
										_this.content = res.data.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									} else {
										uni.showModal({
											title: '提示',
											content: res.data.msg
										})
									}
								}
							});
							})
						}
			
			
					}
				})
			},
			
			
			
			
			
			
			
			
			
			
			
			
			
			chooseImage(num) {
				let _this = this
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['camera', 'album'], //这要注意，camera掉拍照，album是打开手机相册
					success: (res) => {
						// uni.setStorageSync('uid', '1002')
						if (uni.getStorageSync("uid") == undefined || uni.getStorageSync("uid") == '') {
							uni.showToast({
								title: '请登录后再试',
								icon: "none"
							});
							return
						}

						if (num == 1||num==3) {
							uni.uploadFile({
								url: "http://36.138.201.130:9999/api/general/textRec?uid=" + uni
									.getStorageSync("uid"), //仅为示例，非真实的接口地址
								filePath: res.tempFilePaths[0],
								name: 'file',
								header: {
									ContentType: "image/jpg"
								},
								success: function(res) {
									let a = JSON.parse(res.data);
									console.log(a, '文字识别结果');
									if (a.code == 200) {
										_this.show = true
										_this.content = a.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									}

								},
								fail: (err) => {
									console.log('请求error_______________', err)
								},
							});
						} else {
							this.detailImage( res.tempFilePaths[0], (data)=>{
								var image=data.replace(/^data:image\/\w+;base64,/, "");
							uni.request({
								url: "http://36.138.201.130:9999/api/general/caption?image=" +image, //服务器端地址itemRec
								data: {
							 
								},
								 
								header: {
									 "Content-Type": "application/json"
								},
								method: 'POST',
								success: (res) => {
									console.log(res);
									if (res.data.code == 200) {
										// uni.showModal({
										// 	title: '识别成功',
										// 	content: res.data.data 
										// })
										_this.show = true
										_this.content = res.data.data
										_this.$api.ttsText({
											text: _this.content
										}).then((res) => {
											_this.innerAudioContext.autoplay = true;
											console.log(res.data.data +
												"==================")
											_this.innerAudioContext.src = res.data.data
											_this.innerAudioContext.onPlay(() => {
												console.log('开始播放');
											});
											_this.innerAudioContext.onError((res) => {
												console.log(res.errMsg);
												console.log(res.errCode);
											});
										})
									} else {
										uni.showModal({
											title: '提示',
											content: res.data.msg
										})
									}
								}
							});
							})
						}


					}
				})
			},
			changeTabIndex(index) {
				this.tabIndex = index
				if (this.tabIndex == 2) {
					this.history();
				}
			},
			swiperChangeIndex(e) {
				this.tabIndex = e.detail.current;
				if (this.tabIndex == 2) {
					this.history();
				}
			},

			// 初始化数据
			initData() {
				this.collectedArr = getApp().globalData.collectedBooks
			}
		},
		onLoad() {
			this.innerAudioContext = uni.createInnerAudioContext();
			this.initData()
			this.history()
		},
		mounted() {
			$U.calSurplusHeight({
				pageID: this,
				pos: 'cal',
				isTabBarPage: true,
				success: val => this.calHeight = val
			})
		}
	}
</script>

<style lang="less">
	.history {
		.title {
			margin-top: 5rpx;
			font-size: 30rpx;
		}

		.line {
			height: 2rpx;
			background-color: #dedede;
		}

		view:first-child {
			font-size: 36rpx;
			color: #0056B3;
		}

		.time {
			font-size: 26rpx;
			color: #a6a6a6;
		}
	}

	.content {
		height: 600rpx;
		width: 640rpx;
		background-color: #d3d3d3;
		position: absolute;
		top: 300rpx;
		left: 25rpx;
		border-radius: 30rpx;
		padding: 20rpx 30rpx;
		font-size: 70rpx;
	}

	.mycamera {
		padding-top: 80upx;
		padding-left: 130upx;
	}

	.mytext1 {
		margin-left: 175rpx; 
		width: 420rpx;
		height: 170rpx;
		color: #008080;
		font-size: 70rpx;
	}
	
	.mytext2 {
		margin-left: 175rpx; 
		width: 420rpx;
		height: 170rpx;
		color: #008080;
		font-size: 70rpx;
	}
</style>
