<template>
	<view>
		<page-title>{{title}}</page-title>
		<bookList :bookListArr="loadedBookList"></bookList>
		<uni-load-more :status="loadMoreStatus"></uni-load-more>
	</view>
</template>

<script>
	import bookList from '@/components/bookList.vue';
	import pageTitle from '@/components/pageTitle.vue';
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	export default {
		data() {
			return {
				loadedBookList: [],
				loadMoreStatus: 'loading',
				page: 1,
				size: 10,
				Tpage: 0,
				nclass: '',
				uid:uni.getStorageSync("uid")

			}
		},
		components: {
			bookList,
			pageTitle,
			uniLoadMore
		},
		methods: {
			// 初始化加载
			initLoadMore() {
				console.log(this.nclass + "====================")
				setTimeout(() => {
					this.$api.getNewsPage({
						page: this.page,
						size: this.size,
						nclass: this.nclass
					}).then((res) => {
						this.loadedBookList = res.data.data.news
						this.Tpage = Math.ceil(res.data.data.page.total / this.size)
						if (this.Tpage <= 1) {
							this.loadMoreStatus = 'noMore'
						}
					})
				}, 200);

			},
			// 触底加载
			loadMore() {
				this.page++
				if (this.page <= this.Tpage) {
					this.$api.getNewsPage({
						page: this.page,
						size: this.size
					}).then((res) => {
						this.loadedBookList = this.loadedBookList.concat(res.data.data.news)
					})
				} else {
					this.loadMoreStatus = 'noMore'
				}

				// switch (obj.isLoadMore) {
				// 	case '正在加载中':
				// 		return
				// 		break;
				// 	case '没有更多了':
				// 		this.loadMoreStatus = 'noMore'
				// 		break;
				// 	case '上拉加载显示更多': {
				// 		this.loadMoreStatus = 'loading';
				// 		setTimeout(() => {
				// 			this.loadedBookList = [...this.loadedBookList, ...obj.data];
				// 			this.loadMoreStatus = 'more'
				// 		}, 1500)
				// 	}
				// 	break;
				// }
			}
		},
		onLoad(option) {
			console.log(JSON.stringify(option.name))
			if(option.name !='' && option.name !==undefined){
				this.title = option.name
				this.nclass = option.name
			}else{
					this.title = '新闻'
			}
			
			this.initLoadMore()
		},
		onReachBottom() {
			this.loadMore()
		}
	}
</script>

<style>

</style>
