<script>
	export default {
		globalData: {
			collectedBooks: []
		}, 
		onLaunch: function() {
			console.log('App Launch')
			uni.setStorageSync("baseUrl","http://36.138.201.130:9999")
			// uni.setStorageSync("uid","11")
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
	/* 自定义css库 */
	@import url("./common/css/free.css");
	/* 矢量图标库 */
	@import url("./common/css/iconfont.css");
	/* css动画库 */
	@import url("./common/css/animate.css");
	/* 自定义css动画库 */
	@import url("./common/css/myanimate.css");
	/* 自定义css主题 */
	@import url("./common/css/theme.css");
	@import "/common/zcm-main.css";
</style>
