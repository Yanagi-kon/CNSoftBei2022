import Vue from 'vue'
import store from 'store'
import App from './App'
import myIcon from '@/components/myIcon.vue';
import request from '@/common/request';
import request2 from '@/common/request2';

Vue.config.productionTip = false;

App.mpType = 'app';
Vue.prototype.$statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
Vue.prototype.$http = request;
Vue.prototype.$http2 = request2;
import api from '@/uni_modules/vmeitime-http/' //请求拦截
Vue.prototype.$api = api
Vue.prototype.audioplay = function(path){
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = true;
	innerAudioContext.src = path;
	innerAudioContext.onPlay(() => {
	  console.log('开始播放');
	});
	innerAudioContext.onEnded(() => {
	innerAudioContext.stop()
	 console.log('播放结束!');
	
	 })
	innerAudioContext.onError((res) => {
	  console.log(res.errMsg);
	  console.log(res.errCode);
	});
}
Vue.prototype.audiostop = function(){
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = false;
	innerAudioContext.stop()
	
}
Vue.prototype.btnClick = function(text){
	this.$api.ttsText({
		text:text
	}).then((res) => {
		this.audioplay(res.data.data);
	})
}


Vue.component('myIcon',myIcon);

const app = new Vue({
	store,
	...App
}).$mount()
