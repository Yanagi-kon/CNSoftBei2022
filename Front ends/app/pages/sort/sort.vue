<template>
	<view>
		<view class="cal" :style="{height: `${statusBarHeight}px`}"></view>
		<search-box class="cal"></search-box>
		<view class="cal" style="height: 20rpx;"></view>

		<view class="flex" style="background-color: #f0f3f8;">
			<!-- 左侧 -->
			<scroll-view scroll-y class="font text-light-black" style="width: 180rpx;padding: 0 20rpx;" :style="{height: `${calHeight}rpx`}">
				<block v-for="(item, index) in leftListRes" :key="index">
					<view class="animated faster" :class="leftIndex === index ? 'selected pulse font-weight-bold':''" style="height: 150rpx;line-height: 150rpx;"
					 @tap="leftToright(index)">{{item}}</view>
				</block>
			</scroll-view>

			<!-- 右侧 -->
			<scroll-view scroll-y :scroll-into-view="rightIndex" scroll-with-animation :style="{height: `${calHeight}rpx`}"
			 @scroll="rightToleft">
				<block v-for="(item, index) in rightListRes" :key="index" >
					<view style="height: 250rpx;margin-bottom: 70rpx;" :id="`right${index}`">

						<!-- 顶部标题 -->
						<view class="text-center" style="height: 80rpx;line-height: 80rpx;" @tap='openNews(item.text)'>
							<text class="mr-1">{{item.text}}</text>
							<my-icon iconId="icon-youjiantou" iconSize="40"></my-icon>
						</view>

						<!-- 底部内容 -->
						<view @tap='openNews(item.text)' class="flex image flex-wrap bg-white rounded font" style="height: 170rpx;">
							
							<image :src="item.content" mode=""></image>
							
						</view>
					</view>
				</block>
				<!-- 占位 -->
				<view :style="{height: `${calHeight - 320}rpx`}"></view>
			</scroll-view>


		</view>
	</view>
</template>

<script>
	import $U from '@/common/unit';
	import {sortResources as resources} from './sortResources';
	import searchBox from '@/components/searchBox.vue'; 
	export default {
		data() {
			return {
				stop: true,
				show_r: true, 
				statusBarHeight: this.$statusBarHeight,
				calHeight: 0,
				leftIndex: 0,
				innerAudioContext: '',
				rightIndex: `right${0}`
			}
		},
		components: {
			searchBox
		},
		
		onShow(){
			this.innerAudioContext = uni.createInnerAudioContext();
			this.innerAudioContext.autoplay = true;
			this.innerAudioContext.src = '../../static/xwfl.mp3';// res.data.data
			this.innerAudioContext.onPlay(() => {
							console.log('开始播放');
						});
			this.innerAudioContext.onError((res) => {
							console.log(res.errMsg);
							console.log(res.errCode);
						});
		},
		 
		
		computed: {
			// 左侧数据
			leftListRes() {
				let res = resources.map(item => item.text)
				return res
			},
			// 右侧数据
			rightListRes() {
				return resources
			}
		},
		methods: {
			openNews(name){
				uni.navigateTo({
					url:'../readLIst/readLIst?name='+name
				})
			},
			// 左联动右
			leftToright(i) {
				this.rightIndex = `right${i}`
			},
			// 右联动左
			rightToleft(e) {
				let curScrollTop = e.detail.scrollTop,
					standardVal = $U.Topx(320),
					curIndex = Math.round(curScrollTop / standardVal);
				this.leftIndex = curIndex
			}
		},
		mounted() {
			// 动态获取高度
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
	.image{
		image{
			width: 550rpx;
			height: 230rpx;
			border-radius: 10rpx;
			
		}
	}
	.selected {
		color: #f7646d;
	}
</style>
