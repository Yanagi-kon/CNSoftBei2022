<template>
	<view class="fixed-bottom rounded mx-2 mb-1" style="height: 160rpx;background-color: #d1ccc0;opacity: .9;" @tap="toDetailPage"
	 :style="{bottom: `${windowBottom}px`}">
	 
		<!-- 进度部分 -->
		<view class="flex align-center justify-center font" style="color: #7a8388;height: 65rpx;" @tap.stop>
			<!-- 总时长 -->
			<view>{{durationTime | formatTime}}</view>
			<!-- 进度条部分 -->
			<view style="width: 500rpx;">
				<slider block-size="16" activeColor="#e48267" backgroundColor="#eef2f3" :max="durationTime" :value="currentTime"
				 @change="sliderToPlay" @changing="sliderToPlay" />
			</view>
			<!-- 播放时刻 -->
			<view>{{currentTime | formatTime}}</view>
		</view>

		<view class="flex align-center justify-between mx-2" style="height: 95rpx;">
			<!-- 音频相关信息 -->
			<view class="font" style="color: #424651;">
				<view>歌手-{{singerName}}</view>
				<view>歌曲-{{audioName}}</view>
			</view>
			<!-- 音频按钮部分 -->
			<view class="flex align-center" @tap.stop>
				<view class="animated" hover-class="pulse">
					<my-icon iconId="icon-shangyishou" iconSize="75" @my-click="PreOrNext('pre')"></my-icon>
				</view>
				<my-icon :iconId="playStatus?'icon-bofang':'icon-ziyuan'" iconSize="75" class="mx-2" @my-click="PlayOrPause"></my-icon>
				<view class="animated" hover-class="pulse">
					<my-icon iconId="icon-xiayishou" iconSize="75" @my-click="PreOrNext('next')"></my-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters,
		mapMutations,
		mapActions
	} from 'vuex';
	import $U from '@/common/unit';
	export default {
		data() {
			return {
				windowBottom: uni.getSystemInfoSync().windowBottom
			}
		},
		filters: {
			formatTime(num) {
				return num === 100 ? $U.formatTime(0) : $U.formatTime(num)
			}
		},
		computed: {
			...mapState({
				playStatus: ({audio}) => audio.playStatus,
				durationTime: ({audio}) => audio.durationTime,
				currentTime: ({audio}) => audio.currentTime
			}),
			...mapGetters([
				'audioName',
				'singerName'
			])
		},
		methods: {
			...mapMutations([
				'destruction'
			]),
			...mapActions([
				'init',
				'PlayOrPause',
				'PreOrNext',
				'sliderToPlay'
			]),
			toDetailPage() {
				uni.navigateTo({
					url: '/pages/musicDetail/musicDetail'
				});
			}
		},
		mounted() {
			this.init()
		},
		// 这里destroy改为beforeDestroy（destroy时实例被销毁, this会拿不到）
		beforeDestroy() {
			this.destruction()
		}
	}
</script>

<style>

</style>
