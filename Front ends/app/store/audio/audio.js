//音频资源
import http from '@/common/request'

let audio,
	timeout;
export default {
	state: {
		playStatus: false,   //播放与暂停标识
		currentPlayIndex: 0, //当前歌曲标识
		durationTime: 100, 	 // 音频总时长
		currentTime: 0, 	 // 音频播放时刻
		audioList: [], 		 // 音频列表
		// 音频资源
		musics: [{
			id: 1,
			name: "",
			src: '',
			singer: {
				name: "",
				synopsis: ""
			}
		}]
	},
	getters: {
		// 音频name
		audioName(state) {
			let curIndex = state.currentPlayIndex;
			return state.musics[curIndex].name
		},
		// 歌手name
		singerName(state) {
			let curIndex = state.currentPlayIndex;
			let singer = state.musics[curIndex].singer;
			return singer.name
		},
		// 歌手简介
		singerSynopsis(state) {
			let curIndex = state.currentPlayIndex;
			let singer = state.musics[curIndex].singer;
			return singer.synopsis
		},
		// 音频封面
		CurCover(state){
			let curIndex = state.currentPlayIndex;
			return state.musics[curIndex].cover
		}
	},
	mutations: {
		// 销毁
		destruction() {
			audio.offPlay();
			audio.offPause();
			audio.offStop();
			audio.offEnded();
			audio.offCanplay();
			audio.offTimeUpdate();
			audio.offError()
		},
		// 音频播放
		audioPlay(state) {
			let curIndex = state.currentPlayIndex;
			state.audioList[curIndex].playStatus = 1;
			if(audio.src != state.musics[curIndex].src){
				audio.src = state.musics[curIndex].src;
			}
			audio.play()
		},
		// 音频暂停
		audioPause(state) {
			let curIndex = state.currentPlayIndex;
			state.audioList[curIndex].playStatus = -1;
			audio.pause()
		},
		// 音频停止
		audioStop(state) {
			let curIndex = state.currentPlayIndex;
			state.audioList[curIndex].playStatus = 0;
			audio.stop()
		},
		// 音频跳转
		audioSeek(state, pos) {
			audio.seek(pos)
		},
		
		// 改变播放与暂停标识
		changerPlayStatus(state, Boolean) {
			state.playStatus = Boolean
		},
		// 改变播放标识
		changePlayIndex(state, index) {
			state.currentPlayIndex = index
		},
		// 改变当前时间
		changeCurrentTime(state, time) {
			state.currentTime = time
		},	
		
		// 获取音频资源
		getMusic(state, musicResourecs){
			state.musics = musicResourecs
		},
		// 获取总时长
		getDurationTime(state, time) {
			state.durationTime = time
		},
		// 获取音频列表
		getAudioList(state, audioList) {
			for (let item of audioList) {
				state.audioList.push({
					id: item.id,
					audioName: item.name,
					singerName: item.singer.name,
					playStatus: 0 // -1 -> 暂停 | 0 -> 停止 | 1 -> 播放
				})
			}
		},
	},
	actions: {
		//初始化
		async init({state,commit,dispatch}) {
			if (audio) return;
			// 获取资源
			let {musicResourecs} = await http.get('/musicResourecs'); 
			commit('getMusic', musicResourecs)
			
			audio = uni.createInnerAudioContext(); //实例化audio对象
			// #ifdef H5
			audio.src = state.musics[0].src;
			// #endif
			commit('getAudioList', state.musics); // 获取音频列表
			
			// 监听
			// 音频可以播放事件
			audio.onCanplay(() => {
				// #ifdef MP-WEIXIN
				commit('getDurationTime', audio.duration);
				commit('changeCurrentTime', audio.currentTime)
				// #endif
			})
			// 音频播放事件
			audio.onPlay(() => {
				commit('changerPlayStatus', true);
				console.log('开始播放')
			})
			// 音频暂停事件
			audio.onPause(() => {
				commit('changerPlayStatus', false);
				console.log('暂停播放')
			})
			// 音频停止事件
			audio.onStop(() => {
				commit('changerPlayStatus', false);
				console.log('停止播放')
			})
			// 音频结束事件
			audio.onEnded(() => {
				commit('changerPlayStatus', false);
				dispatch('PreOrNext', 'next');
				console.log('播放结束')
			})
			// 音频错误事件
			audio.onError((res) => {
				console.log(res.errMsg);
				commit('changerPlayStatus', false);
				console.log(res.errCode)
			})
			// 音频时间更新事件
			audio.onTimeUpdate(() => {
				commit('getDurationTime', audio.duration);
				commit('changeCurrentTime', audio.currentTime)
			})
		},
		// 播放与暂停
		PlayOrPause({state,commit}) {
			state.playStatus ? commit('audioPause') : commit('audioPlay')
		},
		// 切歌
		PreOrNext({state,commit}, type) {
			commit('audioStop');
			let curIndex = state.currentPlayIndex,
				lastIndex = state.musics.length - 1;
			switch (type) {
				case 'pre':
					curIndex === 0 ? commit('changePlayIndex', lastIndex) : commit('changePlayIndex', curIndex - 1)
					break;
				case 'next':
					curIndex === lastIndex ? commit('changePlayIndex', 0) : commit('changePlayIndex', curIndex + 1)
					break;
			}
			commit('audioPlay')
		},
		// 拖动操作
		sliderToPlay({state,commit}, {detail: {value: position}}) {
			let time = position;
			commit('audioSeek', position);
			// app 打包后seek将不触发TimeUpdate 所以干脆都走commit('changeCurrentTime', time)
			clearTimeout(timeout);
			timeout = setTimeout(() => commit('changeCurrentTime', time), 200)
		},
		// 列表选择播放
		selectPlay({state,commit}, id) {
			let curIndex = state.musics.findIndex(item => item.id === id);
			if (state.currentPlayIndex === curIndex) {
				
				state.playStatus ? commit('audioPause') : commit('audioPlay')
				
				return
			} else {
				commit('audioStop');
			}
			commit('changePlayIndex', curIndex);
			commit('audioPlay')
		}
	}
}
