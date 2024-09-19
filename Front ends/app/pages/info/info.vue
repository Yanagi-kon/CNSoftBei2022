<template>
	<view class="changeInfo">
		<view class="nav-box">
			<!-- <view class="title">编辑个人资料</view> -->
			<navigator open-type="switchTab" url="/pages/personal/personal" class="iconfont iconfanhui icon-nav">
			</navigator>
		</view>
		<view class=" box-c d-ac-c d-ai-c d-jc-c">
			<view class="img-box ">
				<image class="img" :src="src" style="width: 200rpx;height: 200rpx;border-radius: 100%;"></image>
				<view class="text"></view>
			</view>
		</view>
		<view class="info-box">
			<navigator open-type="redirect" url="/pages/modify/modify?title=userName" class="text-box">
				<view class="left">用户名</view>

				<view class="right">
					<input class="right" type="text" v-model="user.nickname" value="" />
				</view>
			</navigator> 
			<picker :range="level" @change="bindLevelChange">
				<view class="text-box  box d-jc-sb">
					<view class="left">视障等级</view>
					<view class="right">{{user.vision}}</view>
				</view>
			</picker>
			<navigator open-type="redirect" url="/pages/modify/modify?title=userPhone" class="text-box">
				<view class="left">手机号码</view>
				<view class="iconfont iconchangyongicon- icon-box"></view>
				<view class="right">
					<input class="right" type="text" value="" v-model="user.tel" />
				</view>
			</navigator>
			<navigator open-type="redirect" url="/pages/modify/modify?title=intrduction" class="text-box">
				<view class="left">简介</view>
				<view class="iconfont iconchangyongicon- icon-box"></view>
				<view class="right">
					<input type="text" class="right" value="" v-model="user.introduction" />
				</view>
			</navigator>
			<navigator open-type="redirect" url="/pages/modify/modify?title=userEmail" class="text-box">
				<view class="left">邮箱</view>
				<view class="iconfont iconchangyongicon- icon-box"></view>
				<view class="right">
					<input type="text" class="right" value="" v-model="user.email" />
				</view>
			</navigator>
			<picker :range="sex" @change="bindSexChange">
				<view class="text-box box d-jc-sb">
					<view class="left">性别</view>
					<view class="right">{{user.sex}}</view>
				</view>
			</picker>
			<picker mode="date" :value="user.birthday" @change="bindDateChange">
				<view class="text-box box d-jc-sb">
					<view class="left">生日</view>
					<view class="right">{{user.birthday}}</view>
				</view>
			</picker>
			<pca-picker-cc v-on:getAddressResultObj="getAddressResultObj" :addressList.sync="addressList">
				<view  class="box d-jc-sb address">
					<view class="uni-input">地区</view>
					<view class="uni-input">{{ addressList.join(' ') }}</view>
				</view>
			
			</pca-picker-cc>
			<view class="btn">
				<button type="default" @tap='save'>保存</button>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				src: '../../static/user/girl.png',
				user: {},
				sex: ['男', '女'],
				city: ['北京', '上海', '深圳', '广州', '南京'],
				level: ['一级低视力', '二级低视力', '一级盲', '二级盲'],
				addressList: ["", "", ""],
			}
		},
		onShow() {
			this.getUserInfo()
			console.log(JSON.stringify(this.user))

		},
		methods: {
			getAddressResultObj(e) {
				//获取详细的 省/市/区 对象信息,更新时被动调,主动调用请自行设置ref值,方法一致
			
				this.user.province = e.province.name
				this.user.city = e.city.name
				this.user.district = e.county.name
			},
			save() {
				debugger
				this.$api.personalCenter(this.user).then((res) => {
					if (res.data.code == 200) {
						uni.switchTab({
							url: '../mine/mine'
						})
					} else {
						uni.showToast({
							title: res.data.message,
							icon: "none"
						});
					}

				})
			},
			getUserInfo() {
				// this.$api.getUser({
				// 	uid: 1000
				// }).then((res) => {
				// 	this.user = res.data.data

				// })
				let uid = uni.getStorageSync('uid')
				if (uid == '') {
					uni.navigateTo({
						url: '../login/login'
					})

				} else {
					this.$api.getUser({
						uid: uid
					}).then((res) => {
						this.user = res.data.data
						this.addressList = [this.user.province,this.user.city,this.user.district]

					})
				}

			},
			chooseImg() {
				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					sizeType: ['original', 'compressed'],
					success: (res) => {
						this.src = res.tempFilePaths
					}
				})
			},
			bindLevelChange(e) {
				this.user.vision = this.level[e.detail.value]

			},
			bindSexChange(e) {
				this.user.sex = this.sex[e.target.value]
			},
			bindDateChange(e) {
				this.user.birthday = e.target.value
			},
			bindCityChange(e) {
				//this.user.city=e.target.value[0]
				this.user.area = this.city[e.target.value]

			}
		}
	}
</script>

<style lang="less">
	.address{
		font-size: 30rpx;
	}
	.btn {
		button {
			margin-top: 100rpx;
			width: 600rpx;
			border-radius: 50rpx;
			background-color: #d1d1d1;
		}
	}

	.changeInfo {
		width: 100%;
		height: 100%;
		background: #FFFFFF;
	}

	.nav-box {
		height: 30px;
		position: relative;
		margin-top: 50;
		padding-top: 12px;
	}

	.title {
		text-align: center;
		color: #000000;
		font-size: 20px;
	}

	.icon-nav {
		position: absolute;
		top: 30px;
		left: 10px;
		color: #000000;
	}



	.img-box {
		text-align: center;
	}

	.img {
		margin-top: 15px;
		width: 70px;
		height: 70px;
		border-radius: 50%;
	}

	.text {
		margin-top: 2px;
		color: #999999;
		font-size: 13px;
	}

	.info-box {
		padding: 0 10px;
	}

	.text-box {
		width: 100%;
		height: 52px;
		line-height: 52px;

	}

	.left {
		float: left;
		font-size: 15px;
		color: #000000;
	}

	.right {
		float: right;
		font-size: 18px;
		margin-right: 20rpx;
		height: 100rpx;
		color: #999999;

		input {
			text-align: right;
		}
	}

	.icon-box {
		float: right;
		font-size: 15px;
		color: #999999;
		width: 10px;
	}
</style>
