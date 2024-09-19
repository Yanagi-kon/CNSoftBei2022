export default {
	data() {
		return {
			currentPage: 1,
			pageSize: 5
		}
	},
	methods: {
		_getList() {

		},
		resetData() {
			this.currentPage = 1;
			this.pageSize = 5;
		},
		toPage(url, success) {
			uni.navigateTo({
				url,
				success
			})
		},
		backPage() {
			uni.navigateBack({

			})
		},
		showLoading(title) {
			uni.showLoading({
				title: title || '加载中',
				mask: true
			});
		},
		hideLoading() {
			uni.hideLoading();
		}
	}
}
